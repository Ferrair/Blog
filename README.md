This is an Android project of my `Blog-System`.


##Using `MVP` to build all architecture.
 - `Model`：including remote and local.
 - `View`：Android XML file and some custom-view.
 - `Presenter`：`LoadPresenter` that can upload and download data from server.And then call the callback method that hooked by `Activity` for passing this data to View layer.(So `Activity` can be seen as Controller layer)


##Using `OkHttp`,`Retrofit` fragment to fetch JSON from my server.
 - fetch JSON data from my server
 - parse JSON data to `JsonArray`
 - show the data by a adapter


##Generics Adapter in this project
`AdapterPool` is a Pool that hold a lot of `AdapterProvider`. `AdapterProvider` can be holden by use `register(Model.class, new AdapterProvider(this))`.
So some bind work is delegate to `AdapterProvider` while `AdapterPool` can do something generics like add data or setListener,etc.

see wqh.blog.ui.adapter package.

see [MultiType](https://github.com/drakeet/MultiType/blob/master/library/src/main/java/me/drakeet/multitype/TypeItemFactory.java)



