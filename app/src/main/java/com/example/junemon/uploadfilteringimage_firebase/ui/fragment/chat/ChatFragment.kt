package com.example.junemon.uploadfilteringimage_firebase.ui.fragment.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.defaultMessageLimit
import com.example.junemon.uploadfilteringimage_firebase.MainApplication.Companion.mDatabaseReference
import com.example.junemon.uploadfilteringimage_firebase.R
import kotlinx.android.synthetic.main.fragment_chat.view.*


class ChatFragment : Fragment(), ChatFragmentView {
    private lateinit var presenter: ChatFragmentPresenter
    private val userKeyPass = "asdwafads"
    private var userName: String? = null
    private var ctx: Context? = null

    fun newInstance(userName: String?): ChatFragment {
        val bundle = Bundle()
        val fragment = ChatFragment()
        bundle.putString(userKeyPass, userName)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        userName = args?.getString(userKeyPass)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = ChatFragmentPresenter(mDatabaseReference, this)
        presenter.onAttach(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val views: View = inflater.inflate(R.layout.fragment_chat, container, false)
        presenter.onCreateView(views)
        return views
    }

    override fun initView(view: View) {
        view.etChatBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.toString()?.trim()?.length!! > 0) {
                    view.ivSendMessage.visibility = View.VISIBLE
                }
            }
        })
        view.etChatBox?.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(defaultMessageLimit)))
        view.ivSendMessage?.setOnClickListener {
            presenter.sendUserMessage(view.etChatBox?.text.toString(), userName)
            view.etChatBox.text = Editable.Factory.getInstance().newEditable("")
        }
    }

}