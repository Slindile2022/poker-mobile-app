package com.poker.ui.session

import androidx.lifecycle.LiveData
import com.poker.ui.base.BaseListViewModel

class SessionDetailsViewModel: BaseListViewModel<SessionDetailsListItem>() {
    override val isLoading: LiveData<Boolean>
        get() = TODO("Not yet implemented")
    override val error: LiveData<String?>
        get() = TODO("Not yet implemented")
    override val items: LiveData<List<SessionDetailsListItem>>
        get() = TODO("Not yet implemented")

    override fun refresh() {
        TODO("Not yet implemented")
    }
}