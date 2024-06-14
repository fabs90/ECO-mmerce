require('dotenv').config();

const express = require('express');
const bodyParser = require('body-parser');
const loadModel = require('./model/loadModel'); 

const authRoutes = require('./routes/authRoutes');
const dataRoutes = require('./routes/dataRoutes');
const productRoutes = require('./routes/product');
const modelRoutes = require('./routes/modelRoutes'); 

const app = express();

app.use(bodyParser.json());

app.use('/auth', authRoutes);
app.use('/api', dataRoutes);
app.use('/products', productRoutes);
app.use('/model', modelRoutes);

(async () => {
  try {
    const model = await loadModel();
    app.locals.model = model;
    console.log('Model loaded and stored in app.locals.');

    // Start the server
    const PORT = process.env.PORT || 3000;
    app.listen(PORT, () => {
      console.log(`Server is running on port ${PORT}`);
    });
  } catch (error) {
    console.error('Error loading model:', error);
  }
})();
