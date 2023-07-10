package io.john6.sample.loadimage

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ReadTestViewModel(private val application: Application) :AndroidViewModel(application){
    private val readTestUiState = MutableStateFlow(ReadTestUiState())

    val uiState =
        readTestUiState.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ReadTestUiState()
        )

    fun onUiEvent(event: ReadTestUIEvent){
        when(event){
            is ReadTestUIEvent.ShowChooseTypeDialog -> {
                readTestUiState.value = readTestUiState.value.copy(showChooseTypeDialog = event.show)
            }
            is ReadTestUIEvent.SelectType -> {
                readTestUiState.value = readTestUiState.value.copy(selectedTypeIndex = event.index)
            }

            is ReadTestUIEvent.LoadImage -> {
                viewModelScope.launch {
                    val res = getAllImageInfoFromLocalStorage(
                        application.contentResolver,
                        event.desireUriList
                    )
                    readTestUiState.update {
                        it.copy(imageInfoList = res)
                    }
                }
            }

            is ReadTestUIEvent.ShowImageDetailDialog -> {
                readTestUiState.value =
                    readTestUiState.value.copy(desiredImageInfoToShowInDialog = event.desiredImageInfoToShowInDialog)
            }

            is ReadTestUIEvent.ChooseMedia -> {
                readTestUiState.value =
                    readTestUiState.value.copy(doChooseMedia = event.shouldStart)
            }
        }
    }
}

sealed class ReadTestUIEvent{
    data class ShowChooseTypeDialog(val show: Boolean): ReadTestUIEvent()
    data class ShowImageDetailDialog(val desiredImageInfoToShowInDialog: ImageInfo? = null): ReadTestUIEvent()
    data class SelectType(val index: Int): ReadTestUIEvent()
    data class LoadImage(val desireUriList: List<Uri> = emptyList()): ReadTestUIEvent()
    data class ChooseMedia(val shouldStart:Boolean): ReadTestUIEvent()
}

/**
 * 读取测试的UI状态
 * @param typeList 所有的选择类型
 * @param imageInfoList 所有选择列表
 * @param showChooseTypeDialog 是否显示选择类型的对话框
 * @param desiredImageInfoToShowInDialog 选择的图片信息
 * @param selectedTypeIndex 选择的类型在[typeList]中的下标
 */
@Stable
@Immutable
data class ReadTestUiState(
    val isLoading: Boolean = false,
    val error: String = "",
    val empty: Boolean = false,

    val typeList: List<String> = listOf(
        "Custom",
        "ACTION_PICK",
        "ACTION_GET_CONTENT",
        "PickMultipleVisualMedia"
    ),
    val imageInfoList: List<ImageInfo> = emptyList(),
    val showChooseTypeDialog: Boolean = false,
    val selectedTypeIndex: Int = 0,
    val doChooseMedia: Boolean = false,
    val desiredImageInfoToShowInDialog: ImageInfo? = null,
)