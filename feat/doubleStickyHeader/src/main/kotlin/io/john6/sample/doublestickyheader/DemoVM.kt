package io.john6.sample.doublestickyheader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.john6.base.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class DemoVM:ViewModel() {

    private val job1:Job
    private val job2:Job
    init {
        job1 = viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            (0 .. 10000).forEach{
                try {
                    ensureActive()
                } catch (e: Exception) {
                    if (it % 200 == 0) {
                        "start1 cancelled:${it}".log()
                    }
                }
                if (it % 200 == 0) {
                    "start1:${it}".log()
                }
            }
            "finally start1".log()
        }
        job2 = viewModelScope.launch {
            (0 .. 10).forEach{
                "start2:$it".log()
                delay(500L)
            }
        }

        viewModelScope.launch {
            delay(520L)
            job1.cancel()
            "job1 cancel".log()
            delay(1300L)
            job2.cancel()
        }

    }
}