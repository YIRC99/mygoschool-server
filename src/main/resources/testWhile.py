import time
import os

os.environ['TF_ENABLE_ONEDNN_OPTS'] = '0'
import opennsfw2 as n2


def is_image_file(file_path):
    image_extensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp']
    _, extension = os.path.splitext(file_path)
    return extension.lower() in image_extensions


def isCorrectImgPath(imgPath):
    # 检查路径是否存在
    if not os.path.exists(imgPath):
        print(f"fail path( {imgPath} )is not exists")
        return False

    # 检查是否为图片文件
    if not is_image_file(imgPath):
        print(f"fail( {imgPath} )not is image file")
        return False

    return True


def main():
    while True:
        # 等待用户输入图片路径
        img_path = input().strip()

        if img_path == 'exit':
            break

        # 对参数进行检查
        if not isCorrectImgPath(img_path):
            continue  # 如果路径不正确，则重新等待输入

        # 运行检查
        start_time = time.time()
        imageResult = n2.predict_image(img_path)
        end_time = time.time()
        elapsed_time = end_time - start_time
        # print(f"image {img_path} result {imageResult} time {elapsed_time:.6f} second")
        print(imageResult)


if __name__ == '__main__':
    main()
