#!flask/bin/python
from flask import Flask, request
from apiKayo import sendCloud
import gc
import os
import json
from datetime import datetime
import threading
import warnings
from keras.models import load_model
import numpy as np
from keras import backend as K

warnings.filterwarnings('ignore')

app = Flask(__name__)
loaded_model = None

def load():
	global loaded_model
	loaded_model=load_model('model2.hdf5')
	loaded_model._make_predict_function() 


def apiKayo(X_test):
  

  X_test = np.array(X_test)
  X_test = X_test.reshape((1,X_test.shape[0],X_test.shape[1]))						
  #loaded_model=load_model('model2.hdf5')
  #print(loaded_model.summary())
  predictions = loaded_model.predict(X_test)
  y_predict_category=[np.argmax(t) for t in predictions]

  #K.clear_session()
  return y_predict_category

def convert(o):
    if isinstance(o, numpy.int64): return int(o)  
    raise TypeError

@app.route('/')
def index():
    return "Hello Api Kayo"

@app.route('/test', methods=['POST'])
def testModel():
	# current date and time
	now = datetime.now()
	timestampINFog = datetime.timestamp(now)
	dados = request.get_json()
	dados['timestampINFog'] = timestampINFog
	X = dados.get('lista')
	r = apiKayo(X)
	now = datetime.now()
	timestampOutFog = datetime.timestamp(now)
	dados['timestampOUTFog'] = timestampOutFog
	dados['result'] = str(r[0])
	
	x = threading.Thread(target=sendCloud, args=(dados,))
	x.start()

	del X 
	gc.collect()
	#return json.dumps({'results': numpy.int64(r[0])}, default=convert)
	return str(r[0])



if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    load()
    app.run(host='0.0.0.0', port=port)







