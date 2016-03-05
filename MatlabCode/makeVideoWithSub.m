logoLeft = imread('sub10_left.bmp');
logoRight = imread('sub10_right.bmp');

% no of frames is 720
% time duration is 30s
% sub1: 00:00:00,750 --> 00:00:03,780
% sub2: 00:00:03,780 --> 00:00:06,290
% sub3: 00:00:06,290 --> 00:00:07,550
% sub4: 00:00:07,550 --> 00:00:10,980
% sub5: 00:00:10,980 --> 00:00:12,780
% sub6: 00:00:12,780 --> 00:00:15,030
% sub7: 00:00:15,030 --> 00:00:17,510
% sub8: 00:00:23,740 --> 00:00:24,770
% sub9: 00:00:24,770 --> 00:00:26,550
% sub10: 00:00:29,500 --> 00:00:31,440
time_begin = 28.500;
time_end = 30;
time_lower = round(720 * time_begin/30);
time_higher = round(720 * time_end/30);

%logoLeft = imresize(logoLeft, 0.25);

[logoRows, logoColumns, dummy]=size(logoLeft);

bigbangObj = VideoReader('left.avi');
bigbangDat = read(bigbangObj);
disp(size(bigbangDat));
[frameHeight, frameWidth, noOfChannels, NoOfFrames] = size(bigbangDat);
disp(bigbangObj.NumberOfFrames);

disp('begin left');
watermark = logoLeft;
%watermark=  imresize(logoLeft, 0.2);
[watermarkRow, watermarkColumn, dumdum] = size(watermark);

% make alpha channel for left image
%alphaLeft=read(bigbangObj,1);
alphaLeft=zeros(frameHeight,frameWidth);
%pause;
for  i =1:frameHeight
    for j=1:frameWidth
        alphaLeft(i,j) = 0;
    end
end

for i=1:watermarkRow
    for j=1:watermarkColumn
        if (watermark(i,j))~=0
            alphaLeft(round((frameHeight-watermarkRow)/2)+i,round((frameWidth-watermarkColumn)/2)+j)= 1;
        end
    end
end
imshow(alphaLeft,[]);
%pause;


% create the video writer with 1 fps
writerObj = VideoWriter('left_1');
writerObj.FrameRate = 23;
% open the video writer
open(writerObj);

% for left video, place logo at bottom right corner
% for i = 1:NoOfFrames
% %for i = 200:210
%     disp(i);
%     videoFrame = read(bigbangObj,i);
%     
%     watermark = imresize(logoLeft , 0.2);
%     %imshow(watermark);
%     % Replace pixels at bottom right
%     [sourceRow, sourceColumn, dum] = size(videoFrame);
%     [watermarkRow, watermarkColumn, dumdum] = size(watermark);
%     videoFrame((sourceRow-watermarkRow + 1):end,( sourceColumn-watermarkColumn + 1):end, 1:end) = watermark; 
%     
%     
%     
%     newFrame = im2frame(videoFrame);
%     %imshow(videoFrame);
%     %pause;
%     writeVideo(writerObj, newFrame);
% end


for i = 1:NoOfFrames
%for i = 200:210
    disp(i);
    videoFrame = read(bigbangObj,i);
    [sourceRow, sourceColumn, dum] = size(videoFrame);
    if ((i >= time_lower) && (i <= time_higher))
        for j = 1:watermarkRow
            for k=1:watermarkColumn
                rowIdx = round((sourceRow-watermarkRow)/2)+j;
                colIdx = round((sourceColumn-watermarkColumn)/2)+k;
                videoFrame(rowIdx,colIdx, 1) = alphaLeft(rowIdx,colIdx) * watermark(j,k,1) + (1-alphaLeft(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 1);
                videoFrame(rowIdx,colIdx, 2) = alphaLeft(rowIdx,colIdx) * watermark(j,k,2) + (1-alphaLeft(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 2);
                videoFrame(rowIdx,colIdx, 3) = alphaLeft(rowIdx,colIdx) * watermark(j,k,3) + (1-alphaLeft(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 3);
            end
        end
    end
    %videoFrame(((sourceRow-watermarkRow)/2 + 1):(sourceRow+watermarkRow)/2 + 1,( (sourceColumn-watermarkColumn)/2 + 1):(sourceColumn+watermarkColumn)/2, 1:end) = watermark; 
    
    newFrame = im2frame(videoFrame);
    %imshow(videoFrame);
    %pause;
    writeVideo(writerObj, newFrame);
end

% close the writer object
close(writerObj);


bigbangObj = VideoReader('right.avi');
bigbangDat = read(bigbangObj);
disp(size(bigbangDat));
[frameHeight, frameWidth, noOfChannels, NoOfFrames] = size(bigbangDat);
disp(bigbangObj.NumberOfFrames);

disp('begin right');
watermark = logoRight;
%watermark=  imresize(logoRight, 0.2);
[watermarkRow, watermarkColumn, dumdum] = size(watermark);

% make alpha channel for right image
%alphaLeft=read(bigbangObj,1);
alphaRight=zeros(frameHeight,frameWidth);
%pause;
for  i =1:frameHeight
    for j=1:frameWidth
        alphaRight(i,j) = 0;
    end
end

for i=1:watermarkRow
    for j=1:watermarkColumn
        if (watermark(i,j))~=0
            alphaRight(round((frameHeight-watermarkRow)/2)+i,round((frameWidth-watermarkColumn)/2)+j)= 1;
        end
    end
end


% create the video writer with 1 fps
writerObj = VideoWriter('right_1');
writerObj.FrameRate = 23;
% open the video writer
open(writerObj);

% for right video, place logo at bottom right corner
% for i = 1:NoOfFrames
% %for i = 200:210
%     disp(i);
%     videoFrame = read(bigbangObj,i);
%     
%     watermark = imresize(logoRight , 0.2);
%     %imshow(watermark);
%     % Replace pixels at bottom right
%     [sourceRow, sourceColumn, dum] = size(videoFrame);
%     [watermarkRow, watermarkColumn, dumdum] = size(watermark);
%     videoFrame((sourceRow-watermarkRow + 1):end,( sourceColumn-watermarkColumn + 1):end, 1:end) = watermark; 
%     
%     
%     
%     newFrame = im2frame(videoFrame);
%     %imshow(videoFrame);
%     %pause;
%     writeVideo(writerObj, newFrame);
% end


for i = 1:NoOfFrames
%for i = 200:210
    disp(i);
    videoFrame = read(bigbangObj,i);
    [sourceRow, sourceColumn, dum] = size(videoFrame);
    if ((i >= time_lower) && (i <= time_higher))
        for j = 1:watermarkRow
            for k=1:watermarkColumn
                rowIdx = round((sourceRow-watermarkRow)/2)+j;
                colIdx = round((sourceColumn-watermarkColumn)/2)+k;
                videoFrame(rowIdx,colIdx, 1) = alphaRight(rowIdx,colIdx) * watermark(j,k,1) + (1-alphaRight(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 1);
                videoFrame(rowIdx,colIdx, 2) = alphaRight(rowIdx,colIdx) * watermark(j,k,2) + (1-alphaRight(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 2);
                videoFrame(rowIdx,colIdx, 3) = alphaRight(rowIdx,colIdx) * watermark(j,k,3) + (1-alphaRight(rowIdx,colIdx)) * videoFrame(rowIdx,colIdx, 3);
            end
        end
    end
    %videoFrame(((sourceRow-watermarkRow)/2 + 1):(sourceRow+watermarkRow)/2 + 1,( (sourceColumn-watermarkColumn)/2 + 1):(sourceColumn+watermarkColumn)/2, 1:end) = watermark; 
    
    newFrame = im2frame(videoFrame);
    %imshow(videoFrame);
    %pause;
    writeVideo(writerObj, newFrame);
end

% close the writer object
close(writerObj);


I = imread('1.png'); 
watermark = imread('logoL.bmp'); 
% Scale watermark if needed 
% I scaled mine to 1/5 its original size 
watermark = imresize(watermark , 0.2); 
% Replace pixels at bottom right
[sourceRow, sourceColumn, dum] = size(I);
[watermarkRow, watermarkColumn, dumdum] = size(watermark);
%I((size(I,1)-size(watermark,1) + 1):end,( size( I, 2)-size( watermark, 2) + 1):end, 1:end) = watermark; 
I((sourceRow-watermarkRow + 1):end,( sourceColumn-watermarkColumn + 1):end, 1:end) = watermark; 
%imshow(I);








% % load the images
% images    = cell(3,1);
% images{1} = imread('im1.png');
% images{2} = imread('im2.png');
% images{3} = imread('im3.png');
% 
% % create the video writer with 1 fps
% writerObj = VideoWriter('myVideo.avi');
% writerObj.FrameRate = 1;
% % set the seconds per image
% secsPerImage = [5 10 15];
% % open the video writer
% open(writerObj);
% % write the frames to the video
% for u=1:length(images)
%  % convert the image to a frame
%  frame = im2frame(images{u});
%  for v=1:secsPerImage(u) 
%      writeVideo(writerObj, frame);
%  end
% end
% % close the writer object
% close(writerObj);