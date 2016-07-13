# Cardboard
- Android app for viewing 3D images, videos and panorama images using Google Cardboard 
- Originally it was a project for my Computational Photography class, so it was all native Java code instead of using Unity
- Matlab code was written for generating stereoscope images and videos using DIBR Algorithm 
- A simple PhotoSphere was implemented using gyroscope sensors for viewing panorama images 

## Image Cardboard
The default images are stored in a folder called **CardBoardDemo** in the home directory. And the image names are _imgLeft.bmp_ and _imgRight.bmp_. In the app menu, you can choose your own images and when entering viewing mode, you can switch the default images and your chosen pictures by triggering the magent. 

## Video Cardboard
The default videos are stored in a folder called **CardBoardDemo** in the home directory. And the video names are _vidLeft.mp4_ and _vidRight.mp4_. In the menu, you can choose your own videos and when entering the viewing mode, you can play and pause the video by triggering the magnet. 

## Panaorma Cardboard
The default image is stored in a folder called **CardBoardDemo** in the home directory . And the image aame is _pana.jpg_. In the menu, you can choose your own image and when enrering the viewing mode you can zoom in and out by triggering the magnet. 

Also, in order to minimize the interference of the magnet (of the CardBoard), gyroscope sensor is used to determine the orientation instead of using accelerometer and magentet sensor in the given code. 

### In the code folder:
- **findVideoInfo.m** - to find the information of a video
- **generateSubPic.m** - generate a black image with a given line of text, resolution is the same as the video, I call it a subtitle image
- **logoDepthMap.m** - generate depth map for the CUHK logo
- **makeVideoWithLogo.m** - paste the CUHK logo (left and right) on the video (left and right)
- **makeVideoWithSub.m** - blend the subtitle image (left and right) into the video (left and right) 
- **subDepthMap.m** - generate depth map for a subtitle image
- **withDepthMap.m** - generate images (left and right) using a given depth map 
- **withDisparityMap.m** - generate images (left and right) using a given disparity map

### In the CardboardFolder:
You can just copy this whole folder into the sdcard directory of your device.
- **CUHK/left.bmp** - original image, which is used as the left image
- **CUHK/right.bmp**    - image generated using withDepthMap.m, which is used as the right image
- **OrgVideo/left.mp4** - the original given video
- **OrgVideo/right/mp4** - the original given video
- **Pana/pan2.jpg** - another panaroma image to choose
- **Sample/left.bmp**   - the sample image given
- **Sample/left.jpg** - the sample image given
- **Sample/right.bmp** - the sample image given
- **Sample/right.jpg** - the sample image given
- **Test1/left.bmp** - an given image
- **Test1/right.bmp** - image generated using with DepthMap.m, which is used as the right image
- **Test2/left.bmp** - an given image
- **Test2/right.bmp** - image generated using with DepthMap.m, which is used as the right image
- **VideoTest/OnlyLogo/left.mp4** - video generated using makeVideoWithLogo.m, with CUHK logo on it
- **VideoTest/OnlyLogo/right.mp4** - video generated using makeVideoWithLogo.m, with a processed CUHK logo (i.e. CUHK/right.bmp) on it
- **imgLeft.bmp** - default left image for Image Cardboard
- **imgRight.bmp** - default right image for Image Cardboard
- **panLeft.jpg** - default left image for Panaroma Cardboard
- **panRight.jpg** - default right image for Panaroma Cardboard
- **vidLeft.mp4** - default left video for Video Cardboard
- **vidRight.mp4** - default left video for Video Cardboard

Note: On Android 6.0, you have to manually enable the permission for Location and Storage after installation in order for the app to work.
