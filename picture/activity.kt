package ru.reckchant.digitalskills

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import ru.reckchant.digitalskills.databinding.ActivityPictureBinding

class PictureActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPictureBinding.inflate(layoutInflater) }
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { picture ->
                if (picture != null) {
                    binding.pictureImageView.setImageBitmap(picture)

                    val inputImage = InputImage.fromBitmap(picture, 0)
                    recognizer.process(inputImage)
                            .addOnSuccessListener { visionText ->
                                binding.resultTextView.text = visionText.text
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            }
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.takePictureButton.setOnClickListener {
            takePicture.launch(null)
        }

        binding.menuButton.setOnClickListener {
            finish()
        }
    }
}