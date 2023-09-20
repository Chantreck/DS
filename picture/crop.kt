private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { picture ->
            if (picture != null) {
                binding.pictureImageView.setImageBitmap(picture)
            }
        }

val returnedBitmap = Bitmap.createBitmap(binding.imageView.width, binding.imageView.height, Bitmap.Config.ARGB_8888)
val canvas = Canvas(returnedBitmap)
binding.imageView.draw(canvas)
var rect = Rect()
binding.scrollView2.getLocalVisibleRect(rect)
val resultBitmap: Bitmap = Bitmap.createBitmap(
        returnedBitmap, rect.left,
        rect.top, rect.right, rect.bottom-rect.top
)
binding.imageView2.setImageBitmap(resultBitmap)

//masking
val mask = BitmapFactory.decodeResource(resources, R.drawable.exclude)
val result = Bitmap.createBitmap(mask.width, mask.height, Bitmap.Config.ARGB_8888)
val mCanvas = Canvas(result)
val paint = Paint(Paint.ANTI_ALIAS_FLAG)
paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
mCanvas.drawBitmap(resultBitmap, 0f, 0f, null)
mCanvas.drawBitmap(mask, 0f, 0f, paint)
paint.xfermode = null
binding.imageView3.setImageBitmap(result)

binding.takePictureButton.setOnClickListener {
    takePicture.launch(null)
}