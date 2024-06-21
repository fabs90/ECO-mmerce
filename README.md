# Product Recommendation Model 
This repository was created for **Machine Learning** aspect for ECO-mmerce capstone project. The project was created using tensorflow and python as the main language.

# Dataset
The dataset is from [Kaggle Flipkart Product Dataset](https://www.kaggle.com/datasets/PromptCloudHQ/flipkart-products) that consists 20.000 products.

# Data Preprocessing
The data was preprocessed using Pandas library by **concatenating the product features** that was suitable for the model. the information from the concatenated dataframe will be used for the recommendation model. We want to keep all products inside the dataset, so we decided to use **Imputation** method for filling null values in order to keep all product exists in the dataset. 

# Model Development
We load a pre-trained **DistillBERT** embedding for the model due to its' efficiency because it used less RAM usage than regular BERT embedding and the performance difference is not too far from regular BERT embedding.
We did **Batch tokenize text** to maintain the efficiency of the training process
We get the embedding for the input layer of our neural network
We built our own neural network using Keras layers with the following structure. <br>

![image](https://github.com/BaronBram/Capstone_ML/assets/118114064/71b6b803-bb0a-4bd7-8928-c56f4dcacc49)
