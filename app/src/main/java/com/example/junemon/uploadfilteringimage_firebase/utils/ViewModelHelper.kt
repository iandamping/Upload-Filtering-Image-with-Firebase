package com.example.junemon.uploadfilteringimage_firebase.utils


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

inline fun <reified T : ViewModel> FragmentActivity.viewModelHelperForActivity(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.withViewModel(body: T.() -> Unit): T {
    val vm = viewModelHelperForActivity<T>()
    vm.body()
    return vm
}

inline fun <reified T : ViewModel> Fragment.viewModelHelperForFragment(): T {
    return ViewModelProviders.of(this).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.withViewModel(body: T.() -> Unit): T {
    val vm = viewModelHelperForFragment<T>()
    vm.body()
    return vm
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

//inline fun <reified T : ViewModel> FragmentActivity.withViewModel(crossinline factory: () -> T, body: T.() -> Unit): T {
//    val vm = getViewModelWithFactory(factory)
//    vm.body()
//    return vm
//}
//inline fun <reified T : ViewModel> Fragment.withViewModel(crossinline factory: () -> T, body: T.() -> Unit): T {
//    val vm = getViewModelWithFactory(factory)
//    vm.body()
//    return vm
//}
//
//
//inline fun <reified T : ViewModel> Fragment.getViewModelWithFactory(crossinline factory: () -> T): T {
//
//    val vmFactory = object : ViewModelProvider.Factory {
//        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
//    }
//
//    return ViewModelProviders.of(this, vmFactory)[T::class.java]
//}
//
//inline fun <reified T : ViewModel> FragmentActivity.getViewModelWithFactory(crossinline factory: () -> T): T {
//
//    val vmFactory = object : ViewModelProvider.Factory {
//        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
//    }
//
//    return ViewModelProviders.of(this, vmFactory)[T::class.java]
//}





