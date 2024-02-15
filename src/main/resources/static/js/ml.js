const fileInput = document.getElementById('cat');
const modelURL = "js/model.json";
const metadataURL = "js/metadata.json";

let model;

fileInput.addEventListener('change', async function () {
    const file = fileInput.files[0];
    if (!file) return;
    const url = URL.createObjectURL(file);
    
    const imagePreview = document.getElementById('imagePreview');
    imagePreview.src = url;
    
    const prediction = await model.predict(imagePreview);
    
    let max = prediction[0];
    for (let i = 1; i < model.getTotalClasses(); i++) {
        if (max.probability < prediction[i].probability) {
            max = prediction[i];
        }
    }
    console.log(max.className);
    document.getElementById("resu").textContent = max.className;
});

async function init() {
    model = await tmImage.load(modelURL, metadataURL);
}