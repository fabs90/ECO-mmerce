const tf = require('@tensorflow/tfjs-node');

class L2 {
    static className = 'L2';

    constructor(config) {
        return tf.regularizers.l1l2(config);
    }
}

tf.serialization.registerClass(L2);
async function loadModel() {
    try {
        const model = await tf.loadLayersModel(process.env.MODEL_URL, {
            customObjects: { L2 }
        });
        console.log('Model loaded successfully:', model);
        return model;
    } catch (error) {
        console.error('Error loading model:', error);
        throw error;
    }
}

module.exports = loadModel;