const getData = async (req, res) => {
  try {
      // Ensure the model is loaded
      const model = req.app.locals.model;
      if (!model) {
          return res.status(500).json({ message: 'Model is not loaded yet' });
      }

      const products = model.data; 

      if (!products || products.length === 0) {
          return res.status(404).json({ message: 'No products found' });
      }

      const response = products.map(product => ({
          produk_id: product.id,
          produk_name: product.name,
          produk_url: product.link
      }));

      res.json(response);
  } catch (error) {
      console.error('Error while getting data:', error);
      res.status(500).json({ message: 'Error while getting data', error });
  }
};

module.exports = {
  getData
};
