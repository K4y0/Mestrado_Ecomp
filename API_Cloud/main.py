#!flask/bin/python
from flask import Flask, request
import gc
import os
import json
import numpy
from datetime import datetime
import threading
import csv 
import warnings

warnings.filterwarnings('ignore')

app = Flask(__name__)

def convert(o):
    if isinstance(o, numpy.int64): return int(o)  
    raise TypeError


def salvarCSV(dados):
	csv_file = "dadoscsv.csv"
	with open(csv_file, 'a') as f:
		writer = csv.writer(f)  # Note: writes lists, not dicts.
		#for data in dados:  # Maybe your df, or whatever iterable.
		writer.writerow([dados.get('lista'), dados.get('timestamp'), dados.get('timestampINFog'), dados.get('timestampOUTFog'), dados.get('timestampINCloud'), dados.get('result')])

@app.route('/')
def index():
    return "Hello, World!"

@app.route('/test', methods=['POST'])
def testModel():
	# current date and time
	now = datetime.now()
	timestampINCloud = datetime.timestamp(now)
	dados = request.get_json()
	dados['timestampINCloud'] = timestampINCloud
	salvarCSV(dados)
	gc.collect()
	#return json.dumps({'results': numpy.int64(r[0])}, default=convert)
	return 'ok'

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5050))
    app.run(host='0.0.0.0', port=port)








