package com.binabola.app.presentation.StrengthExercise

import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.StayCurrentLandscape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.data.model.Exercise
//import com.binabola.app.data.remote.model.DetailExercise
//import com.binabola.app.data.remote.response.AllExerciseRespone
import com.binabola.app.ml.PoseDetectorProcessor
import com.google.android.gms.tasks.TaskExecutors
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.atan2

@OptIn(ExperimentalGetImage::class)
@Composable
fun StrengthExercise(
    workoutId: Long,
    navigateToHome: () -> Unit,
    viewModel: InteractiveLearnViewModel = hiltViewModel()
    ) {

    val lens by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }
    val configuration = LocalConfiguration.current
    LocalContext.current
    val exerciseState by viewModel.exercise.collectAsState(initial = Result.Loading)
    val uiState by viewModel.uiState.collectAsState()
    val onCounting by viewModel.onCounting.collectAsState()
    val maxRepetition by viewModel.maxRepetition.collectAsState()
    val maxSet by viewModel.maxSet.collectAsState()


    when (exerciseState) {
        is Result.Loading -> {
            viewModel.getExerciseById(workoutId)
        }

        is Result.Success<*> -> {
            val data = (exerciseState as Result.Success<Exercise>).data

            viewModel.initialateCounter(
                data.id,
                data.interactiveSetting.repetion, data.interactiveSetting.set
            )
            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    Row {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) {
                            data.let {
                                CameraPreview(
                                    exercise = it,
                                    isSafeZone = (uiState.isTutorialScreen || uiState.isInRestMode || uiState.isFinished),

                                    updateCounter = {
                                        if (uiState.currentSet != maxSet) {
                                            viewModel.increaseCount()
                                        }
                                    },
                                    cameraLens = lens
                                )
                            }

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(
                                    start = 55.dp
                                ),
                        ) {


                            if (uiState.isTutorialScreen && !onCounting) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    when (uiState.tutorialStep) {
                                        1 -> {
                                            Image(
                                                painter = painterResource(id = R.drawable.tutorial_1),
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(200.dp)
                                            )
                                            Text(
                                                "For optimal calculations, ensure your smartphone aligns with the illustration above.\n",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            FilledTonalButton(
                                                onClick = {
                                                    viewModel.nextStepTutotrial()
                                                },
                                                elevation = ButtonDefaults.buttonElevation(
                                                    defaultElevation = 2.dp
                                                ),
                                                shape = RoundedCornerShape(12.dp),
                                                contentPadding = PaddingValues(
                                                    vertical = 14.dp,
                                                    horizontal = 49.dp
                                                ),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = neutral80,
                                                    contentColor = neutral10
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    "Next",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = FontFamily.Default,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                        2 -> {

                                            Image(
                                                painter = painterResource(id = R.drawable.tutorial_step2_illus),
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(120.dp)
                                            )
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Text(
                                                    "Light blue box: Exercise not started or in rest section ",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    "Red box: Exercise in progress, but movement is incorrect.",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    "Green box: Correct or near-correct movement and will  automatic counter increment",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                            FilledTonalButton(
                                                onClick = {
                                                    viewModel.nextStepTutotrial()
                                                },
                                                elevation = ButtonDefaults.buttonElevation(
                                                    defaultElevation = 2.dp
                                                ),
                                                shape = RoundedCornerShape(12.dp),
                                                contentPadding = PaddingValues(
                                                    vertical = 14.dp,
                                                    horizontal = 49.dp
                                                ),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = neutral80,
                                                    contentColor = neutral10
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    "Next",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontFamily = FontFamily.Default,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                        else -> {

                                            Column(
                                                modifier = Modifier.padding(top = 52.dp)
                                            ) {
                                                Text(
                                                    "What does the box that appears in the center of the screen mean ?  ",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Spacer(modifier = Modifier.height(16.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)


                                                ) {

                                                    Surface(
                                                        border = BorderStroke(2.dp, neutral80),
                                                        modifier = Modifier
                                                            .background(neutral80)
                                                            .padding(6.dp)
                                                    ) {
                                                        Icon(
                                                            Icons.Filled.Repeat,
                                                            contentDescription = "Repetisi"
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        "Exercise reps are the counts of movements you perform from the start until the movement is completed once.",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Surface(
                                                        border = BorderStroke(2.dp, neutral80),
                                                        modifier = Modifier
                                                            .background(neutral80)
                                                            .padding(6.dp)
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.stack),
                                                            contentDescription = null,
                                                            tint = neutral10,
                                                            modifier = Modifier
                                                                .width(20.dp)
                                                                .height(20.dp)
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.width(4.dp))

                                                    Text(
                                                        "Sets is  number of repetitions of a particular movement performed in a series.",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(12.dp))

                                                FilledTonalButton(
                                                    onClick = {
                                                        viewModel.startTimer()
                                                    },
                                                    elevation = ButtonDefaults.buttonElevation(
                                                        defaultElevation = 2.dp
                                                    ),
                                                    shape = RoundedCornerShape(12.dp),
                                                    contentPadding = PaddingValues(
                                                        vertical = 14.dp,
                                                        horizontal = 49.dp
                                                    ),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = neutral80,
                                                        contentColor = neutral10
                                                    ),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {


                                                    Text(
                                                        "Double Tap to start  ",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontFamily = FontFamily.Default,
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                }
                                            }

                                        }
                                    }


                                }
                            } else if (onCounting) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()

                                ) {
                                    if (uiState.isInRestMode) {
                                        Text(
                                            "Take a break Until ",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = 24.sp,
                                            )
                                        )
                                    } else {
                                        Text(
                                            "Be prepare in ",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = 24.sp,
                                            )
                                        )
                                    }

                                    Text(
                                        viewModel.currentTimeString,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 28.sp,

                                            )
                                    )
                                }
                            } else if (uiState.isFinished && !onCounting) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        "Applause for you!  It's the first step towards a better version of you.",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textAlign = TextAlign.Center

                                            )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    FilledTonalButton(
                                        onClick = {
                                            navigateToHome()
                                        },
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 2.dp
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(
                                            vertical = 14.dp,
                                            horizontal = 49.dp
                                        ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = neutral80,
                                            contentColor = neutral10
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {


                                        Text(
                                            "Back to home ",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontFamily = FontFamily.Default,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                    }




                        }

                            } else {
                                Column {

                                    GifImage(
//                                        data.video ?: ""
                                        data.video ?: R.drawable.placeholder
                                    )


                                }
                            }


                        }
                    }

                    ScoreBox(
                        currentRepeation = uiState.counter,
                        maxRepeatition = maxRepetition,
                        maxSet = maxSet,
                        currentSet = uiState.currentSet

                    )

                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.StayCurrentLandscape,
                            contentDescription = null,
                            Modifier.size(90.dp)
                        )
                        Text(
                            text = " Rotate your device to landscape mode to enjoy this feature.\n",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        }

        is Result.Error -> {
            Text(stringResource(id = R.string.error_message))
        }
    }
}




/*
    isSafeZone: While still in tutorial mode or rest mode
 */
@Composable
fun CameraPreview(
    exercise: Exercise,
    isSafeZone: Boolean,
    updateCounter: () -> Unit,
    cameraLens: Int
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var sourceInfo by remember { mutableStateOf(SourceInfo(10, 10, false)) }
    var detectedPose by remember { mutableStateOf<Pose?>(null) }
    val previewView = remember { PreviewView(context) }
    var rightArmAngle by remember { mutableDoubleStateOf(0.0) }
    var leftArmAngle by remember { mutableDoubleStateOf(0.0) }
    var leftFootAngle by remember { mutableDoubleStateOf(0.0) }
    var rightFootAngle by remember { mutableDoubleStateOf(0.0) }

    remember(sourceInfo, cameraLens) {
        ProcessCameraProvider.getInstance(context)
            .configureCamera(
                previewView, lifecycleOwner, cameraLens, context,
                setSourceInfo = { sourceInfo = it },
                onPoseDetected = { detectedPose = it }
            )
    }

    Log.d("rightArmAngle", rightArmAngle.toString())
    val soundEffect: MediaPlayer = MediaPlayer.create(context, R.raw.correct).apply {
        setVolume(1.0f, 1.0f)
    }

    if (!isSafeZone) {
        LaunchedEffect(
            key1 = areBodyPartsActive(
                exercise,
                rightArmAngle,
                leftArmAngle,
                rightFootAngle,
                leftFootAngle
            ) && !isSafeZone
        ) {

            delay(800)
            soundEffect.start()
            updateCounter()
        }
    }





    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        with(LocalDensity.current) {
            Box(
                modifier = Modifier
                    .size(
                        height = sourceInfo.height.toDp(),
                        width = sourceInfo.width.toDp()
                    )
                    .scale(
                        calculateScale(
                            constraints,
                            sourceInfo,
                            PreviewScaleType.CENTER_CROP
                        )
                    )
            )
            {
                CameraPreview(previewView)
                DetectedPose(
                    pose = detectedPose,
                    sourceInfo = sourceInfo,
                    onRightArmChange = { angle ->
                        rightArmAngle = angle

                    },
                    onLeftArmChange = { angle ->
                        leftArmAngle = angle
                    },
                    onLeftFootChange = { angle ->
                        leftFootAngle = angle

                    },
                    onRightFootChange = { angle ->
                        rightFootAngle = angle

                    }

                )

                if (isSafeZone) {
                    ColoredBorderBox(
                        modifier = Modifier.fillMaxSize(),
                        borderColor = colorPrimaryGreen
                    )
                } else {
                    ColoredBorderBox(
                        modifier = Modifier.fillMaxSize(),
                        borderColor = if (areBodyPartsActive(
                                exercise,
                                rightArmAngle,
                                leftArmAngle,
                                rightFootAngle,
                                leftFootAngle
                            )
                        ) Color.Green else Color.Red
                    )
                }


            }
        }
    }
}

fun areBodyPartsActive(
    exercise: Exercise,
    rightArmAngle: Double,
    leftArmAngle: Double,
    rightFootAngle: Double,
    leftFootAngle: Double
): Boolean {


    return exercise.bodyPartNeeded.all { bodyPart ->
        when (bodyPart) {
            "right_hand" -> isBodyPartActive(
                exercise.interctiveBodyPartSegmentValue.rightArm?.toDouble() ?: 0.0,
                rightArmAngle
            )

            "left_hand" -> isBodyPartActive(
                exercise.interctiveBodyPartSegmentValue.leftArm?.toDouble() ?: 0.0,
                leftArmAngle
            )

            "right_leg" -> isBodyPartActive(
                exercise.interctiveBodyPartSegmentValue.rightLeg?.toDouble() ?: 0.0,
                rightFootAngle
            )

            "left_leg" -> isBodyPartActive(
                exercise.interctiveBodyPartSegmentValue.leftLeg?.toDouble() ?: 0.0,
                leftFootAngle
            )

            else -> false
        }
    } ?: false
}

fun isBodyPartActive(requiredValue: Double, detectedValue: Double): Boolean {
    val angleTolerance = 10.0

    return requiredValue != 0.0 && isWithinTolerance(detectedValue, requiredValue, angleTolerance)
}

fun isWithinTolerance(detectedAngle: Double, requiredAngle: Double, tolerance: Double): Boolean {
    val lowerBound = requiredAngle - tolerance
    val upperBound = requiredAngle + tolerance
    return detectedAngle in lowerBound..upperBound
}

@Composable
private fun ColoredBorderBox(
    modifier: Modifier,
    borderColor: Color
) {
    Box(
        modifier = modifier
            .border(1.dp, borderColor) // Border color and width
    )
}

@Composable
fun GifImage(
//    gif : String,
    gif : Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(

        painter = rememberAsyncImagePainter(
            model = gif,
//            ImageRequest.Builder(context).data(data = gif).apply(block = {
//                size(coil.size.Size.ORIGINAL)
//            }).build(),
            imageLoader = imageLoader,
            error = painterResource(id = R.drawable.placeholder),
            placeholder = painterResource(id = R.drawable.placeholder)
        ),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
    )

}

@Composable
fun DetectedPose(
    pose: Pose?,
    sourceInfo: SourceInfo,
    onRightArmChange: (Double) -> Unit,
    onLeftArmChange: (Double) -> Unit,
    onLeftFootChange: (Double) -> Unit,
    onRightFootChange: (Double) -> Unit
) {

    if (pose != null) {
        if (pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER) != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 1.dp.toPx()
                val primaryPaint = SolidColor(colorPrimaryGreen)


                val needToMirror = sourceInfo.isImageFlipped
                val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
                val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
                val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
                val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
                val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
                val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
                val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
                val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
                val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
                val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
                val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
                val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

                val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
                val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
                val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
                val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
                val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)
                val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)
                val leftHeel = pose.getPoseLandmark(PoseLandmark.LEFT_HEEL)
                val rightHeel = pose.getPoseLandmark(PoseLandmark.RIGHT_HEEL)
                val leftFootIndex = pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
                val rightFootIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)


                fun drawLine(
                    startLandmark: PoseLandmark?,
                    endLandmark: PoseLandmark?,
                    paint: Brush
                ) {
                    if (startLandmark != null && endLandmark != null) {
                        val startX =
                            if (needToMirror) size.width - startLandmark.position.x else startLandmark.position.x
                        val startY = startLandmark.position.y
                        val endX =
                            if (needToMirror) size.width - endLandmark.position.x else endLandmark.position.x
                        val endY = endLandmark.position.y
                        drawLine(
                            brush = paint,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = strokeWidth,
                        )
                    }
                }

                drawLine(leftShoulder, rightShoulder, primaryPaint)
                drawLine(leftHip, rightHip, primaryPaint)
                // Left body
                drawLine(leftShoulder, leftElbow, primaryPaint)
                drawLine(leftElbow, leftWrist, primaryPaint)
                drawLine(leftShoulder, leftHip, primaryPaint)
                drawLine(leftHip, leftKnee, primaryPaint)
                drawLine(leftKnee, leftAnkle, primaryPaint)
                drawLine(leftWrist, leftThumb, primaryPaint)
                drawLine(leftWrist, leftPinky, primaryPaint)
                drawLine(leftWrist, leftIndex, primaryPaint)
                drawLine(leftIndex, leftPinky, primaryPaint)
                drawLine(leftAnkle, leftHeel, primaryPaint)
                drawLine(leftHeel, leftFootIndex, primaryPaint)
                // Right body
                drawLine(rightShoulder, rightElbow, primaryPaint)
                drawLine(rightElbow, rightWrist, primaryPaint)
                drawLine(rightShoulder, rightHip, primaryPaint)
                drawLine(rightHip, rightKnee, primaryPaint)
                drawLine(rightKnee, rightAnkle, primaryPaint)
                drawLine(rightWrist, rightThumb, primaryPaint)
                drawLine(rightWrist, rightPinky, primaryPaint)
                drawLine(rightWrist, rightIndex, primaryPaint)
                drawLine(rightIndex, rightPinky, primaryPaint)
                drawLine(rightAnkle, rightHeel, primaryPaint)
                drawLine(rightHeel, rightFootIndex, primaryPaint)


                val right = getAngle(
                    pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
                )
                val left = getAngle(
                    pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                    pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                    pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
                )
                val rightFoot = getAngle(
                    pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
                )
                val leftFoot = getAngle(
                    pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                    pose.getPoseLandmark(PoseLandmark.LEFT_KNEE),
                    pose.getPoseLandmark(PoseLandmark.LEFT_FOOT_INDEX)
                )
                getAngle(
                    pose.getPoseLandmark(PoseLandmark.NOSE),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_FOOT_INDEX)
                )



                Log.d("VALUE FOR RIGHT HAND", right.toString())
                Log.d("VALUE FOR LEFT HAND", left.toString())
                Log.d("VALUE FOR Left Foot", leftFoot.toString())
                Log.d("VALUE FOR Right Foot", rightFoot.toString())

                onRightArmChange(right)
                onLeftArmChange(left)
                onLeftFootChange(leftFoot)
                onRightFootChange(rightFoot)

            }
        } else {
            Log.d("pose", "null")
        }


    } else {
        Log.d("pose", "null")
    }
}

fun getAngle(
    firstPoint: PoseLandmark?,
    midPoint: PoseLandmark?,
    lastPoint: PoseLandmark?,
): Double {
    if (firstPoint == null || midPoint == null || lastPoint == null) {
        // Handle the case where any of the landmarks is null
        return 0.0 // You can choose an appropriate default value
    }

    val result = Math.toDegrees(
        (atan2(
            lastPoint.position.y - midPoint.position.y,
            lastPoint.position.x - midPoint.position.x
        )
                - atan2(
            firstPoint.position.y - midPoint.position.y,
            firstPoint.position.x - midPoint.position.x
        )).toDouble()
    )

    var correctedResult = abs(result) // Angle should never be negative

    if (correctedResult > 180) {
        correctedResult =
            360.0 - correctedResult // Always get the acute representation of the angle
    }

    return correctedResult
}

@Composable
fun ScoreBox(
    currentRepeation: Int,
    maxRepeatition: Int,
    currentSet: Int,
    maxSet: Int

) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,


        ) {
        Row(
            modifier = Modifier
                .background(neutral10)
                .wrapContentSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )

        ) {
            Row {
                Icon(Icons.Filled.Repeat, contentDescription = "Repetisi")
                Spacer(modifier = Modifier.width(4.dp))
                Text("$currentRepeation/$maxRepeatition")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Row {
                Icon(
                    painter = painterResource(id = R.drawable.stack),
                    contentDescription = null,
                    tint = neutral10,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("$currentSet/$maxSet")
            }

        }
    }
}

private fun ListenableFuture<ProcessCameraProvider>.configureCamera(
    previewView: PreviewView,
    lifecycleOwner: LifecycleOwner,
    cameraLens: Int,
    context: Context,
    setSourceInfo: (SourceInfo) -> Unit,
    onPoseDetected: (Pose) -> Unit
): ListenableFuture<ProcessCameraProvider> {
    addListener({
        val cameraSelector = CameraSelector.Builder().requireLensFacing(cameraLens).build()

        val preview = androidx.camera.core.Preview.Builder()
            .build()
            .apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

        val analysis =
            bindAnalysisUseCase(cameraLens, setSourceInfo, onPoseDetected)
        try {
            get().apply {
                unbindAll()
                bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                bindToLifecycle(lifecycleOwner, cameraSelector, analysis)
            }
        } catch (exc: Exception) {
            TODO("process errors")
        }
    }, ContextCompat.getMainExecutor(context))
    return this
}

private fun bindAnalysisUseCase(
    lens: Int,
    setSourceInfo: (SourceInfo) -> Unit,
    onPoseDetected: (Pose) -> Unit
): ImageAnalysis? {

    val poseProcessor = try {
        PoseDetectorProcessor()
    } catch (e: Exception) {
        Log.e("+CAMERA", "Can not create pose processor", e)
        return null
    }
    val builder = ImageAnalysis.Builder()
    val analysisUseCase = builder.build()

    var sourceInfoUpdated = false

    analysisUseCase.setAnalyzer(
        TaskExecutors.MAIN_THREAD
    ) { imageProxy: ImageProxy ->
        if (!sourceInfoUpdated) {
            setSourceInfo(obtainSourceInfo(lens, imageProxy))
            sourceInfoUpdated = true
        }
        try {
//                faceProcessor.processImageProxy(imageProxy, onFacesDetected)
            poseProcessor.processImageProxy(imageProxy, onPoseDetected)
        } catch (e: MlKitException) {
            Log.e(
                "CAMERA", "Failed to process image. Error: " + e.localizedMessage
            )
        }
    }
    return analysisUseCase
}

private fun obtainSourceInfo(lens: Int, imageProxy: ImageProxy): SourceInfo {
    val isImageFlipped = lens == CameraSelector.LENS_FACING_FRONT
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    return if (rotationDegrees == 0 || rotationDegrees == 180) {
        SourceInfo(
            height = imageProxy.height, width = imageProxy.width, isImageFlipped = isImageFlipped
        )
    } else {
        SourceInfo(
            height = imageProxy.width, width = imageProxy.height, isImageFlipped = isImageFlipped
        )
    }
}


private fun calculateScale(
    constraints: Constraints,
    sourceInfo: SourceInfo,
    scaleType: PreviewScaleType
): Float {
    val heightRatio = constraints.maxHeight.toFloat() / sourceInfo.height
    val widthRatio = constraints.maxWidth.toFloat() / sourceInfo.width
    return when (scaleType) {
        PreviewScaleType.FIT_CENTER -> kotlin.math.min(heightRatio, widthRatio)
        PreviewScaleType.CENTER_CROP -> kotlin.math.max(heightRatio, widthRatio)
    }
}

@Composable
private fun CameraPreview(previewView: PreviewView) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                previewView.apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }

                previewView
            }
        )


    }
}

data class SourceInfo(
    val width: Int,
    val height: Int,
    val isImageFlipped: Boolean,
)

private enum class PreviewScaleType {
    FIT_CENTER,
    CENTER_CROP
}