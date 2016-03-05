img = im2double(imread('logo.png'));

[rows, columns, dummy] = size(img);

depth_map = zeros(rows, columns);

for i = 1:rows
    for j=1:columns
        if img(i,j) > 0
            depth_map(i,j) = 0.40;
        else
            depth_map(i,j) = 0.46;
        end
    end
end

disp(size(img));
disp(size(depth_map));

imshow(depth_map);
disp(depth_map);
%imshow(depth_map);
imwrite(depth_map,'logo_depth.jpg');