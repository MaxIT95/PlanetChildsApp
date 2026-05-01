package com.example.planetchildsapp.view_model

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planetchildsapp.R
import com.example.planetchildsapp.client.apis.ChildApi
import com.example.planetchildsapp.client.model.request.ChildRequest
import com.example.planetchildsapp.data.repository.UserPrefsRepository
import com.example.planetchildsapp.ui.screen.home.model.ChildModel
import com.example.planetchildsapp.utils.FileUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

data class ChildState(
    val name: String = "",
    val birthday: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val avatarPath: String? = null,
    val avatarUri: Uri? = null,
    val isNameValid: Boolean = true,
    val isBirthdayValid: Boolean = true
)

@HiltViewModel
class ChildViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPrefsRepository: UserPrefsRepository,
    private val childApi: ChildApi,
) : ViewModel() {

    private val _childrenState = MutableStateFlow(ChildState())
    val childState: StateFlow<ChildState> = _childrenState.asStateFlow()

    private val _childrenListState = MutableStateFlow<List<ChildModel>>(emptyList())
    val childrenList: StateFlow<List<ChildModel>> = _childrenListState.asStateFlow()

    // при создании мы идем в API за получением списка детей пользователя
    init {
        Log.i("+++", "Формируем запрос на получение списка из /api/child")
        viewModelScope.launch {
            val response = childApi.getMyChildren()
            if (response.isSuccessful) {
                Log.i("+++", "Ответ успешно получен")
                val childList = response.body()?.content
                Log.i("+++", "childList: $childList")

                val childFromApi = childList?.map { child ->
                    val name = child.name ?: "Имя не указано"
                    val avatarPath = saveChildAvatarLocally(child.id, child.photo)
                    ChildModel(id = child.id, name = name, avatarPath = avatarPath)
                } ?: emptyList()
                _childrenListState.value = childFromApi
            }
        }
    }

    fun onNameChange(name: String) {
        _childrenState.update {
            it.copy(
                name = name,
                isNameValid = name.isNotEmpty()
            )
        }
    }

    fun onBirthdayChange(birthday: String) {
        _childrenState.update {
            it.copy(
                birthday = birthday,
                isBirthdayValid = birthday.isNotEmpty()
            )
        }
    }

    fun onAvatarSelected(avatarUri: Uri) {
        _childrenState.update {
            it.copy(
                avatarUri = avatarUri,
                // Coil умеет грузить content:// URI — прокидываем его в avatarPath,
                // чтобы картинка на экране обновилась сразу после выбора,
                // не дожидаясь копирования файла во внутреннее хранилище при addChild()
                avatarPath = avatarUri.toString()
            )
        }
    }

    fun addChild() {
        viewModelScope.launch {
            val state = _childrenState.value
            if (state.isNameValid && state.isBirthdayValid) {

                _childrenState.update { it.copy(isLoading = true, error = null) }

                Log.i("+++", "Формируем запрос на добавление ребенка...")
                val request = ChildRequest(
                    name = state.name,
                    birthday = state.birthday
                )

                val childJson = request.toString()
                val childPart = MultipartBody.Part.createFormData(
                    "child",
                    null,
                    childJson.toRequestBody("application/json".toMediaTypeOrNull())
                )

                var avatarPart: MultipartBody.Part

                if (state.avatarUri != null) {
                    // сохраняем путь к аватарке в userPrefs
                    val avatarFile = copyPhotoPickerUriToInternalStorage(
                        state.name,
                        state.birthday,
                        state.avatarUri
                    )
                    avatarPart = FileUtils.createMultipartFromFile(avatarFile, "picture")
                } else {
                    avatarPart = FileUtils.createMultipartFromDrawable(context, R.drawable.avatar)
                }

                Log.i("childPart", "$childPart")
                Log.i("avatarPart", "$avatarPart")
                // Вызов API
                val response = childApi.addChild(childPart, avatarPart)

                Log.i("+++", "Ответ по запросу: $response")

                if (response.isSuccessful) {
                    Log.i("+++", "Ответ по запросу успешный")

                    val createdChild = response.body()
                    if (createdChild == null) {
                        _childrenState.update {
                            it.copy(isLoading = false, error = "Пустой ответ сервера")
                        }
                        return@launch
                    }
                    val avatarPath = saveChildAvatarLocally(createdChild.id, createdChild.photo)

                    val newChildModel = ChildModel(
                        id = createdChild.id,
                        name = createdChild.name ?: state.name,
                        birthDate = createdChild.birthday ?: state.birthday,
                        avatarPath = avatarPath
                    )
                    _childrenListState.value += newChildModel
                    _childrenState.update {
                        it.copy(isLoading = false, isSuccess = true, error = null)
                    }
                } else {
                    Log.e("+++", "Ошибка при добавлении ребёнка: ${response.code()}")
                    Log.e("+++", "Ошибка: ${response.errorBody()?.string()}")
                    _childrenState.update {
                        it.copy(
                            isLoading = false,
                            error = "Ошибка при добавлении ребёнка: ${response.code()}"
                        )
                    }
                }
            } else {
                _childrenState.update {
                    it.copy(
                        isLoading = false,
                        error = "Заполните все поля"
                    )
                }
            }
        }
    }

    fun deleteChild(childModel: ChildModel) {
        viewModelScope.launch {
            val response = childApi.deleteChild(childModel.id)
            if (response.isSuccessful) {
                _childrenListState.value =
                    _childrenListState.value.filter { it.id != childModel.id }
            } else {
                Log.e("+++", "Ошибка при удалении ребёнка: ${response.code()}")
            }
        }
    }

    fun clearState() {
        _childrenState.value = ChildState()
    }

    // метод для сохранения во внутреннее хранилище аватарки
    private fun copyPhotoPickerUriToInternalStorage(
        name: String,
        birthday: String,
        uri: Uri
    ): File {
        // Создаём имя файла
        val filename = "avatar_${name}_${birthday}_${System.currentTimeMillis()}.jpg"
        val destination = File(context.filesDir, filename)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            destination.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        _childrenState.value = _childrenState.value.copy(avatarPath = destination.absolutePath)
        viewModelScope.launch {
            userPrefsRepository.saveChildAvatar(filename, destination.absolutePath)
        }
        return destination
    }

    /**
     * Сохраняет аватарку ребёнка из base64 во внутреннее хранилище и возвращает путь к файлу.
     * Если файл уже существует — возвращает его путь без повторного декодирования и записи,
     * тем самым исключая лишнюю работу при каждом создании ViewModel.
     *
     * @param childId  идентификатор ребёнка (используется как часть имени файла)
     * @param base64Photo  строка Base64 с содержимым фото, может быть null
     * @return абсолютный путь к файлу аватарки, либо пустая строка если фото отсутствует
     */
    private fun saveChildAvatarLocally(childId: Long, base64Photo: String?): String {
        if (base64Photo == null) return ""

        val avatarFile = File(context.filesDir, "child_avatar_$childId.jpg")

        // Файл уже был сохранён при предыдущем запросе — повторно не пишем
        if (avatarFile.exists()) {
            Log.i("+++", "Аватарка ребёнка $childId уже кэширована: ${avatarFile.absolutePath}")
            return avatarFile.absolutePath
        }

        return try {
            val avatarBytes = Base64.decode(base64Photo, Base64.DEFAULT)
            avatarFile.writeBytes(avatarBytes)
            Log.i("+++", "Аватарка ребёнка $childId сохранена: ${avatarFile.absolutePath}")
            avatarFile.absolutePath
        } catch (e: IllegalArgumentException) {
            Log.e("+++", "Ошибка декодирования base64 для ребёнка $childId", e)
            ""
        }
    }
}
