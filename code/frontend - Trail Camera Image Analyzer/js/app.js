let images = [];
let currIndex = 0;

function startLabeling() {
    const input = document.getElementById("imageFolder");
    images = Array.from(input.files);

    if (images.length === 0) {
        alert("Please upload your trail camera images folder!");
    } else {
        document.getElementById('labelingSection').style.display = 'block';
        showImage();
    }
}

function showImage() {
    const img = document.getElementById('image-preview');
    const reader = new FileReader();

    reader.onload = function(e) {
        img.scr = e.target.result;
    };

    reader.readAsDataURL(images[currIndex]);
}