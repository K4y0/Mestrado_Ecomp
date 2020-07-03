from keras.models import model_from_json
import numpy as np
from keras import backend as K
import requests
import config
import json




def sendCloud(data):
  
  headers = {'Content-Type': 'application/json', 'Accept':'application/json'}
  resposta = requests.post(config._CONF['host']+":"+str(config._CONF['port'])+"/test", data=json.dumps(data), headers=headers)
  

