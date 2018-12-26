from sqlalchemy import create_engine,inspect
import string
import random
db_source = 'mysql+pymysql://root:root@localhost/tinyurl'
import logging
import time

#your code here


engine = create_engine(db_source)
inspector = inspect(engine)


with open("/tmp/test.csv", "w") as outputfile:
    start = time.perf_counter()
    outputfile.write("url\n")
    for i in range(100000000):
        random_string = ''.join(random.choice(string.ascii_uppercase + string.digits + string.ascii_lowercase) for _ in range(6))

        try:
            outputfile.write(random_string + '\n')
            #engine.execute("insert into urlkey(url) values('%s')" % random_string)
        except:
            logging.error("insert %s is error" % random_string)
    print(time.perf_counter() - start)

