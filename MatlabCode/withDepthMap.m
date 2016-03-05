img  = im2double(imread('sub10.bmp'));
[rows, columns, dummy] = size(img);
%if no disparity map, but depth map is provided
depth_map = im2double(imread('sub10_depth.jpg'));
% real depth value is given by alpha*depth_map(i,j) + beta
alpha = 10;
beta = 0;
depth_mapping = alpha * depth_map + beta;


%sample = im2double(imread('sample.jpg'));
%disp(sample);

% b is distance between two eyes
b = 85;
% f is focal ,length
f = 1;
disparity_map = zeros(rows,columns);
disp(size(depth_mapping));
disp(size(disparity_map));
disparity_map = depth_mapping;
disparity_map = (b * f) ./ disparity_map;
imshow(disparity_map);
%pause;


R = (img(:,:,1));
G = (img(:,:,2));
B = (img(:,:,3));
R = double(R);

disp(size(img));
disp(size(disparity_map));

% imshow(img);
% %pause;
% imshow(disparity_map);
% %pause;

org  = ndgrid(1:rows, 1:columns);
dis_mapping_x = zeros(rows,columns);
dis_mapping_y = [];
%new = img;

% for pixel(i,j), it should be shifred horizontally by j + disparity_map(i,j)
% store these results in mapping
for i=1:rows
    for j=1:columns
        %mapping_y(i,j) = j+disparity_map(i,j);
        dis_mapping_y(i,j) = disparity_map(i,j);
    end
end

mapping_xx = reshape(dis_mapping_x, rows*columns, 1);
mapping_yy = reshape(dis_mapping_y, rows*columns, 1);
%# create coordinate grid for image A
[xx,yy] = meshgrid(1:columns,1:rows);
%# linearize the arrays, and add the offsets
%xx = xx(:);
%yy = yy(:);
disp(size(xx));
disp(size(yy));
xxShifted = xx + dis_mapping_y;
yyShifted = yy;
%# preassign C to the right size and interpolate
R_new = R;
R_new = interp2(xx,yy,R,xxShifted,yyShifted);
G_new = G;
G_new = interp2(xx,yy,G,xxShifted,yyShifted);
B_new = B;
B_new = interp2(xx,yy,B,xxShifted,yyShifted);
final = cat(3, R_new, G_new, B_new);
imshow(R_new);
%pause;
disp(R-R_new);
%pause;
imshow(final);
disp(final-img);


imwrite(img,'left.bmp');
imwrite(final,'right.bmp');