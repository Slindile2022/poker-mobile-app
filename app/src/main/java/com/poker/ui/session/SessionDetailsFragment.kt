package com.poker.ui.session


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.poker.databinding.FragmentSessionDetailsBinding
import com.poker.ui.base.BaseListFragment


class SessionDetailsFragment : BaseListFragment<FragmentSessionDetailsBinding, SessionDetailsViewModel, SessionDetailsListItem>() {
    override val screenTitle: String
        get() = TODO("Not yet implemented")
    override val recyclerView: RecyclerView
        get() = TODO("Not yet implemented")
    override val adapter: RecyclerView.Adapter<*>
        get() = TODO("Not yet implemented")
    override val loadingView: View
        get() = TODO("Not yet implemented")
    override val items: LiveData<List<SessionDetailsListItem>>
        get() = TODO("Not yet implemented")
    override val viewModel: SessionDetailsViewModel
        get() = TODO("Not yet implemented")

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSessionDetailsBinding {
        TODO("Not yet implemented")
    }

    override fun setupUI() {
        TODO("Not yet implemented")
    }

    override fun observeData() {
        TODO("Not yet implemented")
    }
}

}