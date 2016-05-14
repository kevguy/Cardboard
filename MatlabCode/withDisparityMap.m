img  = im2double(imread('3.png'));
disparity_map = im2double(imread('4.png'));


[rows, columns, dummy] = size(img);
R = (img(:,:,1));
G = (img(:,:,2));
B = (img(:,:,3));
R = double(R);

disp(size(img));
disp(size(disparity_map));

% imshow(img);
% pause;
% imshow(disparity_map);
% pause;

org  = ndgrid(1:rows, 1:columns);
dis_mapping_x = zeros(rows,columns);
dis_mapping_y = [];
%new = img;

% for pixel(i,j), it should be shifred horizontally by j + disparity_map(i,j)
% store these results in mapping
for i=1:rows
    for j=1:columns
        %mapping_y(i,j) = j+disparity_map(i,j);
        dis_mapping_y(i,j) = 10 * disparity_map(i,j);
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
%xxShifted = xx;
%yyShifted = yy + dis_mapping_y;

%# preassign C to the right size and interpolate
R_new = R;
R_new = interp2(xx,yy,R,xxShifted,yyShifted);
G_new = G;
G_new = interp2(xx,yy,G,xxShifted,yyShifted);
B_new = B;
B_new = interp2(xx,yy,B,xxShifted,yyShifted);
final = cat(3, R_new, G_new, B_new);
imshow(R_new);
pause;
disp(R-R_new);
pause;
imshow(final);


imwrite(img,'left.bmp');
imwrite(final,'right.bmp');