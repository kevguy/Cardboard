xyloObj = VideoReader('123.avi');
xylDat = read(xyloObj);
disp(size(xylDat));
[frameHeight, frameWidth, noOfChannels, NoOfFrames] = size(xylDat);
disp(xyloObj.NumberOfFrames);