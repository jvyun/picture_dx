package com.example.administrator.kotlintest.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.View.inflate
import android.widget.LinearLayout
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.ALog
import com.dx.banner.newbaselibrary.routerapi.RouterApi
import com.example.administrator.kotlintest.LogConfig
import com.example.administrator.kotlintest.R
import com.example.administrator.kotlintest.adapter.BaseRvAdapter
import com.example.administrator.kotlintest.area.AreaSelectorDialog
import com.example.administrator.kotlintest.channel.ChannelActivity
import com.example.administrator.kotlintest.channel.SQLHelper
import com.example.administrator.kotlintest.dateyearmonthday.AttendviewActivity
import com.example.administrator.kotlintest.dbutil.MeiziDaoUtils
import com.example.administrator.kotlintest.entity.address.AddressAreaEntity
import com.example.administrator.kotlintest.entity.daoentity.Meizi
import com.example.administrator.kotlintest.picture.CropImageActivity
import com.example.administrator.kotlintest.picture.UploadActivity
import com.example.administrator.kotlintest.smashzhadan.smashzhadan
import com.example.administrator.kotlintest.ui.entity.PersonControlDao
import com.example.administrator.kotlintest.ui.entity.学生
import com.example.administrator.kotlintest.widget.DevicesUtils.getSQLHelper
import com.example.baselibrary.MyApplication
import com.example.baselibrary.widgets.ToastUtilKt
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.xfs.fsyuncai.bridge.retrofit.callback.HttpOnNextListener
import com.xfs.fsyuncai.bridge.retrofit.exception.ApiErrorModel
import com.xfs.fsyuncai.bridge.retrofit.http.HttpManager
import com.xfs.fsyuncai.bridge.retrofit.service.OrderService
import com.xfs.qrcode_module.manager.LightManager
import com.xfs.qrcode_module.recycleview.RecycleviewActivity
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.AttrsUtils
import com.yalantis.ucrop.util.PictureMimeType
import jsc.kit.keyboard.KeyBroadActivity
//import jsc.kit.keyboard.KeyBoardView
//import jsc.kit.keyboard.KeyUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.intentFor
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity() : RxAppCompatActivity() {


    private val listData = arrayListOf<PersonControlDao>()
    private var areaSelectorDialog: AreaSelectorDialog? = null
    private val selectedArea = ArrayList<AddressAreaEntity.ListBean>()//已选的区域

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSQLHelper()
        //log 初始化
        LogConfig.initLog(application)

        multipleStatusView?.showLoading()
        listData.add(PersonControlDao("跳转至下一activity", null))
        listData.add(PersonControlDao("日历", null))
        listData.add(PersonControlDao("进入recycleview", null))
        listData.add(PersonControlDao("个人中心", null))
        listData.add(PersonControlDao("Ucrop矩形图片裁剪", null))
        listData.add(PersonControlDao("方形裁剪", null))
        listData.add(PersonControlDao("系统裁剪", null))
        listData.add(PersonControlDao("进入数据库页面", null))
        listData.add(PersonControlDao("频道管理页面", null))
        listData.add(PersonControlDao("点击粉碎当前view", null))
        listData.add(PersonControlDao("点击城市区域选择", null))
        listData.add(PersonControlDao("键盘", null))
        listData.add(PersonControlDao("键盘fragment", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))
        listData.add(PersonControlDao("等", null))

        multipleStatusView.showContent()
        multipleStatusView.setOnClickListener { ToastUtilKt.showCustomToast("点击重新加载") }
        val mainAdapter = MainAdapter(listData, this@MainActivity)

        recycleview_list_all.let {
            it.adapter = mainAdapter
            it.layoutManager = LinearLayoutManager(this)
        }

        mainAdapter.setOnClickItem {
            when (it) {
                0 -> startActivity(this!!.intentFor<AttendviewActivity>())
                1 -> startActivity(this!!.intentFor<ThirdPartBannerZxingAcitivity>())
                2 -> startActivity(this!!.intentFor<RecycleviewActivity>())
                3 -> startActivity(this!!.intentFor<MineActivity>())
                4 -> pickPhotoUcrop()
                5 -> pickPhoto()
                6 -> startActivity(this!!.intentFor<UploadActivity>().putExtra("", "").putExtra("", ""))
                7 -> startActivity(this.intentFor<DbShowActivity>())
                8 -> startActivity(this.intentFor<ChannelActivity>())
                9 -> startActivity(this.intentFor<smashzhadan>())
                10 -> selectCity()
                11 -> startKeyBOard()
                12 -> startKeyBOardFragmentToActivity()
            }
        }
        //view拖拽功能
        dragview.setImageResource(R.drawable.icon_app)
//        mDragView.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495193578123&di=1356056ae967c04aa8b2d75a8634e7a0&imgtype=0&src=http%3A%2F%2Fs15.sinaimg.cn%2Fmw690%2F001MXOZUgy6DUbyFxgy7e%26690");
        dragview.setOnClickListener { Toast.makeText(this@MainActivity, "Clicked me", Toast.LENGTH_SHORT).show() }













        // 跳转至下一activity
        RxView.clicks(tv1).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    tv1.text = "你好"
                    startActivity(this!!.intentFor<ThirdPartBannerZxingAcitivity>())
                }
        //日历
        tv0.setOnClickListener { startActivity(this!!.intentFor<AttendviewActivity>()) }
        //进入recycleview
        tv2.setOnClickListener {
            //            startActivity(this!!.intentFor<WebbrowserActivity4>())
            val 学生 = 学生("丁", 17)
            val (name, age) = 学生
//            ToastUtilKt.showToast("--》${学生.component1()}")
//            ToastUtilKt.showCustomToast("--》${学生.component2()}")
            ToastUtilKt.showToast("--》$name")
            ToastUtilKt.showCustomToast("--》$age")
            startActivity(this!!.intentFor<RecycleviewActivity>())
//            startActivity(this!!.intentFor<com.xfs.fsyuncai.mavendemo.MainActivity>())

        }
        //个人中心
        tv3.setOnClickListener { startActivity(this!!.intentFor<MineActivity>()) }
        //Ucrop矩形图片裁剪
        tv4.setOnClickListener { pickPhotoUcrop() }//打开相册
        //方形裁剪
        tv5.setOnClickListener { pickPhoto() }//打开相册
        //系统裁剪
        tv6.setOnClickListener { startActivity(this!!.intentFor<UploadActivity>().putExtra("", "").putExtra("", "")) }
        tv7.setOnClickListener { startActivity(this.intentFor<DbShowActivity>()) }//进入数据库页面
        tv8.setOnClickListener { startActivity(this.intentFor<ChannelActivity>()) }//频道管理页面
        tv9.setOnClickListener { startActivity(this.intentFor<smashzhadan>()) }//点击粉碎当前view
        tv10.setOnClickListener {
            selectCity()
        }//点击城市区域选择
     ruan.setOnClickListener { startActivity(this!!.intentFor<RuanActivity>()) }
        ying.setOnClickListener { startActivity(this!!.intentFor<YingActivity>()) }
    }

    private fun startKeyBOardFragmentToActivity() {
        startActivity(this!!.intentFor<EmptyFragmentActivity>())
    }

    private fun startKeyBOard() {
//        startActivity(this.intentFor<KeyBroadActivity>())
        ARouter.getInstance().build(RouterApi.KeboardLibrary.ROUTER_KEYBOARD_ACTIVITY_URL)
//                .withInt("select", 4)
                .navigation()
    }

    private fun selectCity() {
        ToastUtilKt.showCustomToast("城市选择")

        areaSelectorDialog = AreaSelectorDialog(this@MainActivity, object : AreaSelectorDialog.ResultCallBack {
            override fun onDismiss() {
            }

            override fun onDismissForResult(dataList: ArrayList<AddressAreaEntity.ListBean>?) {
                selectedArea.clear()
                selectedArea.addAll(dataList!!)
                if (selectedArea.size != 0) {
                    var area = ""
                    selectedArea.map {
                        area += it.name
                    }
                    tv10.text = area
                } else {
                    tv10.text = ""
                }
            }
        }, this@MainActivity)
        areaSelectorDialog!!.show()
    }

    /**
     * 使用相册中的图片
     */
    private val SELECT_PHOTO = 3
    private val UCROP_SELECT_PHOTO = 33
    private var picWith = 310
    private var picHeight = 220
    private var photoUri: Uri? = null
    //图片根目录
    private var sdPath: String? = null
    /***
     * 裁剪图片请求
     */
    val CROP_PHOTO = 2
    /***
     * 从Intent获取图片路径的KEY
     */
    val KEY_PHOTO_PATH = "photo_path"

    constructor(parcel: Parcel) : this() {
        picWith = parcel.readInt()
        picHeight = parcel.readInt()
        photoUri = parcel.readParcelable(Uri::class.java.classLoader)
        sdPath = parcel.readString()
    }

    private fun pickPhoto() {
//        photoUri = getImageUri()
        val intent = Intent(Intent.ACTION_PICK, null)
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, SELECT_PHOTO)
    }

    private fun pickPhotoUcrop() {
//        photoUri = getImageUri()
        val intent = Intent(Intent.ACTION_PICK, null)
        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, UCROP_SELECT_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            //选中图片
            SELECT_PHOTO -> if (data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data.data
                    val selectPhoto = getRealPathFromUri(this, imageUri)
                    Log.e("selectPhoto:", selectPhoto)
//                    startActivity(activity!!.intentFor<ImageDiscernActivity>()
//                            .putExtra("image_path",selectPhoto))
                    //打开裁剪页面
                    startActivity(intentFor<CropImageActivity>()
                            .putExtra("image_path", selectPhoto))
//                        if (selectPhoto != null) {
//                            loadUpImg(selectPhoto)
//                        }
//                    ivAvatar.setImageURI(Uri.fromFile(File(selectPhoto)))
                }
//系统裁剪
//                startPhotoZoom(data.data,picWith,picHeight)
            } else {
//                ToastUtil.showToast("获取图片失败")
                setResult(Activity.RESULT_CANCELED, intent)
//                activity?.finish()
            }
            UCROP_SELECT_PHOTO -> if (data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data.data
                    val selectPhoto = getRealPathFromUri(this, imageUri)
                    Log.e("selectPhoto:", selectPhoto)
                    //图片识别上传至服务器
//                    startActivity(activity!!.intentFor<ImageDiscernActivity>()
//                            .putExtra("image_path",selectPhoto))
                    //自定义的图片裁剪，正方形裁剪
//                    startActivity(intentFor<CropImageActivity>()
//                            .putExtra("image_path",selectPhoto))
                    startCrop(selectPhoto!!)
                }
            } else {
//                ToastUtil.showToast("获取图片失败")
                setResult(Activity.RESULT_CANCELED, intent)
//                activity?.finish()
            }

            //系统裁剪之后--裁剪图片--上传图片
            CROP_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                intent.putExtra(KEY_PHOTO_PATH, photoUri?.path)
                setResult(Activity.RESULT_OK, intent)
//                activity?.finish()
//                tv5.setImageURI(Uri.parse(photoUri?.path))
                loadUpImg(Uri.parse(photoUri?.path).toString())
            } else {
//                ToastUtil.showCustomToast("取消图片设置!")
                setResult(Activity.RESULT_CANCELED, intent)
//                activity?.finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 去裁剪
     *
     * @param originalPath
     */
    @SuppressLint("Range")
    protected fun startCrop(originalPath: String) {
        val options = UCrop.Options()
        val toolbarColor = AttrsUtils.getTypeValueColor(this, -1)
        val statusColor = AttrsUtils.getTypeValueColor(this, -1)
        val titleColor = AttrsUtils.getTypeValueColor(this, -1)
        options.setToolbarColor(toolbarColor)//状态栏背景
        options.setStatusBarColor(statusColor)
        options.setToolbarWidgetColor(titleColor)
        options.setCircleDimmedLayer(false)
        options.setShowCropFrame(true)//显示裁剪框
        options.setShowCropGrid(true)//显示裁剪框网格
        options.setDragFrameEnabled(true)//裁剪框拖拽
        options.setScaleEnabled(true)//图片缩放
        options.setRotateEnabled(true)//图片旋转
        options.setCompressionQuality(70)//图片质量
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(true)//裁剪
        val isHttp = false
        val imgType = PictureMimeType.getLastImgType(originalPath)
        val uri = if (isHttp) Uri.parse(originalPath) else Uri.fromFile(File(originalPath))
        UCrop.of(uri, Uri.fromFile(File(getDiskCacheDir(this), System.currentTimeMillis().toString() + "" + imgType)))
                .withAspectRatio(0F, 0F)
                .withMaxResultSize(0, 0)
                .withOptions(options)
                .start(this)
//        finish()
    }

    private fun getRealPathFromUri(mainActivity: MainActivity, imageUri: Uri?): String? {


        if (mainActivity == null || imageUri == null) {
            return null
        }
        if ("file".equals(imageUri.scheme, true)) {
            return getRealPathFromUri_Byfile(imageUri)
        } else if ("content".equals(imageUri.scheme, true)) {
            return getRealPathFromUri_Api11To18(mainActivity, imageUri)
        }
        return getRealPathFromUri_AboveApi19(mainActivity, imageUri)//没用到
    }

    //针对图片URI格式为Uri:: file:///storage/emulated/0/DCIM/Camera/IMG_20170613_132837.jpg
    private fun getRealPathFromUri_Byfile(uri: Uri): String {
        val uri2Str = uri.toString()
        return uri2Str.substring(uri2Str.indexOf(":") + 3)
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @SuppressLint("NewApi")
    private fun getRealPathFromUri_AboveApi19(context: Context, uri: Uri): String? {
        var filePath: String? = null
        var wholeID: String?
        wholeID = DocumentsContract.getDocumentId(uri)
        // 使用':'分割
        val id = wholeID!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media._ID + "=?"
        val selectionArgs = arrayOf(id)
        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null)
        val columnIndex = cursor.getColumnIndex(projection[0])
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }

    /**
     * //适配api11-api18,根据uri获取图片的绝对路径。
     * 针对图片URI格式为Uri:: content://media/external/images/media/1028
     */
    private fun getRealPathFromUri_Api11To18(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, uri, projection, null, null, null)
        val cursor = loader.loadInBackground()
        if (cursor != null) {
            cursor!!.moveToFirst()
            filePath = cursor!!.getString(cursor!!.getColumnIndex(projection[0]))
            cursor!!.close()
        }
        return filePath
    }


    /**
     * 图片路径传过来
     *
     *
     */
    private fun loadUpImg(path: String) {
        val httpManger = HttpManager.instance()
//        val file = File(BitmapUtils.compressImageUpload(path))
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        httpManger.doHttpDeal(this, httpManger.createService(OrderService::class.java)!!.pictureSearch(body),
                object : HttpOnNextListener() {
                    override fun onNext(json: String) {
                        ToastUtilKt.showCustomToast("Image upload sucessed :$json")
                    }

                    override fun onError(statusCode: Int, apiErrorModel: ApiErrorModel?) {
                        super.onError(statusCode, apiErrorModel)
                        ToastUtilKt.showCustomToast("Image upload failed")
                    }
                }, false)
    }

    /**
     * 系统裁剪
     * uri
     * 宽 本地写死
     * 高 本地写死
     *
     */
    private fun startPhotoZoom(data: Uri?, picWith: Int, picHeight: Int) {

        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(data, "image/*")
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1.4)
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", picWith)
        intent.putExtra("outputY", picHeight)
        intent.putExtra("scale", true)
        photoUri = getImageUri()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        intent.putExtra("return-data", false)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG)
        intent.putExtra("noFaceDetection", true) // no face detection
        ALog.e("TAKE_PHOTO", "TAKE_PHOTO-zoom-CROP_PHOTO")
        startActivityForResult(intent, CROP_PHOTO)
    }

    private fun getImageUri(): Uri? {
        var outputImage: File? = null
        val t = System.currentTimeMillis()
        outputImage = File(sdPath, t.toString() + "xfsImage.jpg")
        if (outputImage!!.exists()) {
            outputImage.delete()
        }
        try {
            outputImage.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(outputImage)
    }

    /**
     * @param context
     * @return
     */
    fun getDiskCacheDir(context: Context): String {
        var cachePath: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            cachePath = context.externalCacheDir!!.path
        } else {
            cachePath = context.cacheDir.path
        }
        return cachePath
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}

class MainAdapter(option: ArrayList<PersonControlDao>, mineActivity: MainActivity)
    : BaseRvAdapter<PersonControlDao>(option, R.layout.item_person_control, mineActivity) {
    override fun onBindView(holder: Companion.BaseRvHolder, data: PersonControlDao) {
        data.text.let {
            holder.setText(R.id.tvText, it)
        }
        data.hint?.let {
            holder.setText(R.id.tvHint, it)
        }
    }
}
