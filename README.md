This is an Android project of my `Blog-System`.


##Using `MVP` to build all architecture.
 - `Model`:including remote and local.
 - `View`:Android XML file and some custom-view.
 - `Presenter`:`LoadPresenter` that can upload and download data from server.And then call the callback method that hooked by `Activity` for passing this data to View layer.(So `Activity` can be seen as Controller layer)


##Using `OkHttp`,`Retrofit` fragment to fetch JSON from my server.
 - fetch JSON data from my server
 - parse JSON data to `JsonArray`
 - show the data by Adapter


##Generics Adapter in this project

see wqh.blog.ui.adapter.BaseAdapter package.

