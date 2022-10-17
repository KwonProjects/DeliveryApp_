package co.kr.kwon.deliveryapp.screen.review

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.ReviewEntity
import co.kr.kwon.deliveryapp.databinding.ActivityAddRestaurantReviewBinding
import co.kr.kwon.deliveryapp.extensions.isVisible
import co.kr.kwon.deliveryapp.screen.base.BaseActivity
import co.kr.kwon.deliveryapp.screen.review.camera.CameraActivity
import co.kr.kwon.deliveryapp.screen.review.gallery.GalleryActivity
import co.kr.kwon.deliveryapp.widget.adapter.PhotoListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddRestaurantReviewActivity :
    BaseActivity<AddRestaurantReviewViewModel, ActivityAddRestaurantReviewBinding>() {

    override val viewModel by viewModel<AddRestaurantReviewViewModel>(){
        parametersOf(
            orderId
        )
    }

    override fun getViewBinding(): ActivityAddRestaurantReviewBinding =
        ActivityAddRestaurantReviewBinding.inflate(layoutInflater)


    private var imageUriList: ArrayList<Uri> = arrayListOf()
    private val firebaseAuth by inject<FirebaseAuth>()
    private val firebaseStorage by inject<FirebaseStorage>()
    private val fireStore by inject<FirebaseFirestore>()

    private val photoListAdapter = PhotoListAdapter { uri -> removePhoto(uri) }

    private val orderId by lazy { intent.getStringExtra(ORDER_ID_KEY)!! }

    private val restaurantTitle by lazy { intent.getStringExtra(RESTAURANT_TITLE_KEY)!! }




    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener { finish() }
        photoRecyclerView.adapter = photoListAdapter

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val userId = firebaseAuth.currentUser?.uid.orEmpty()
            val rating = ratingBar.rating

            showProgress()

            if (imageUriList.isNotEmpty()) {
                lifecycleScope.launch {
                    val result = uploadPhoto(imageUriList)
                    afterUploadPhoto(result, title, content, rating, userId)
                }
            } else {
                uploadArticle(userId, title, content, rating, listOf())
            }
            viewModel.writeReview(orderId)
        }
    }

    private suspend fun uploadPhoto(uriList: List<Uri>) = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> = uriList.mapIndexed { index, uri ->
            lifecycleScope.async {
                try {
                    val fileName = "image${index}.png"
                    return@async firebaseStorage.reference.child("reviews/photo").child(fileName)
                        .putFile(uri)
                        .await()
                        .storage
                        .downloadUrl
                        .await()
                        .toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@async Pair(uri, e)
                }
            }
        }
        return@withContext uploadDeferred.awaitAll()
    }

    private fun afterUploadPhoto(
        results: List<Any>,
        title: String,
        content: String,
        rating: Float,
        userId: String
    ) {
        val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
        val successResults = results.filterIsInstance<String>()

        when {
            errorResults.isNotEmpty() && successResults.isNotEmpty() -> {
                photoUploadErrorButContinueDialog(
                    errorResults,
                    successResults,
                    title,
                    content,
                    rating,
                    userId
                )
            }
            errorResults.isNotEmpty() && successResults.isEmpty() -> {
                uploadError()
            }
            else -> {
                uploadArticle(userId, title, content, rating, successResults)
            }
        }
    }

    private fun uploadArticle(userId: String, title: String, content: String, rating: Float, imageUrlList: List<String>) {
        val reviewEntity = ReviewEntity(
            userId,
            title,
            System.currentTimeMillis(),
            content,
            rating,
            imageUrlList,
            orderId,
            restaurantTitle
        )
        fireStore
            .collection("review")
            .add(reviewEntity)

        Toast.makeText(this, getString(R.string.complete_review), Toast.LENGTH_SHORT).show()
        hideProgress()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryScreen()
                } else {
                    Toast.makeText(this, getString(R.string.reject_permission), Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun startGalleryScreen() {
        startActivityForResult(
            GalleryActivity.newIntent(this),
            GALLERY_REQUEST_CODE
        )
    }

    private fun startCameraScreen() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun showProgress() {
        binding.progressBar.isVisible(true)
    }

    private fun hideProgress() {
        binding.progressBar.isVisible(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                data?.let {
                    val uriList = it.getParcelableArrayListExtra<Uri>("uriList")
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            CAMERA_REQUEST_CODE -> {
                data?.let {
                    val uriList = it.getParcelableArrayListExtra<Uri>("uriList")
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("사진첨부")
            .setMessage("사진첨부할 방식을 선택하세요")
            .setPositiveButton("카메라") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("갤러리") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .create()
            .show()

    }

    private fun photoUploadErrorButContinueDialog(
        errorResults: List<Pair<Uri, Exception>>,
        successResults: List<String>,
        title: String,
        content: String,
        rating: Float,
        userId: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("특정 이미지 업로드 실패")
            .setMessage("업로드에 실패한 이미지가 있습니다." + errorResults.map { (uri, _) ->
                "$uri\n"
            } + "그럼에도 불구하고 업로드 하시겠습니까?")
            .setPositiveButton("업로드") { _, _ ->
                uploadArticle(userId, title, content, rating, successResults)
            }
            .create()
            .show()
    }


    private fun uploadError() {
        Toast.makeText(this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
        hideProgress()
    }

    private fun removePhoto(uri: Uri) {
        imageUriList.remove(uri)
        photoListAdapter.setPhotoList(imageUriList)
    }

    override fun observeData() {

    }


    companion object {
        const val PERMISSION_REQUEST_CODE = 1000
        const val GALLERY_REQUEST_CODE = 1001
        const val CAMERA_REQUEST_CODE = 1002

        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        const val ORDER_ID_KEY = "orderId"

        fun newIntent(
            context: Context,
            orderId: String,
            restaurantTitle: String
        ) = Intent(context, AddRestaurantReviewActivity::class.java).apply {
            putExtra(ORDER_ID_KEY, orderId)
            putExtra(RESTAURANT_TITLE_KEY, restaurantTitle)
        }
    }
}