%sample code
% text = sprintf('fuck');
% H = vision.TextInserter(text);
% H.Color=[1.0 1.0 0];
% H.FontSize = 20;
% H.Location = [25 25];
% 
% I = im2double((imread('1.png')));
% InsertedImg = step(H,I);
% imshow(InsertedImg);


text = sprintf('so ***** with two slits in it\n and either slit is observed,');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub1.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('it will not go through both slits.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub2.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('If it''s unobserved, it will.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub3.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('However,if it''s observed after it''s left\n the plane but before it hits its target,');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub4.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('it will not have gone though both slits.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub5.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('Agreed. What''s your point?');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub6.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('There''s no point. I just think\n it''s a good idea for a t-shirt.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub7.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('Excuse me.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub8.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('Hang on.');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub9.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
text = sprintf('Uh,one across is "aegean. "');
H = vision.TextInserter(text);
H.Color=[1.0 1.0 0];
H.FontSize = 20;
H.Location = [250 130];

%I = im2double((imread('1.png')));
I = zeros(352,632,3);
InsertedImg = step(H,I);
imshow(InsertedImg);
imwrite(InsertedImg, 'sub10.bmp');
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%