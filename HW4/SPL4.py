import sqlite3
import sys
from sqlite3 import Error

def main():
    #args = sys.argv[1:]
    #configFile = open(args[0], "r")
    configFile = open("config.txt", "r")
    configLines = configFile.readlines()
    configFile.close();
    numbers = configLines[0].replace("\n"," ").split(',')
    
    hatsNum = int(numbers[0])
    suppliersNum = int(numbers[1])

    hats = []
    suppliers = []
    
    for i in range(1,hatsNum+1):
        hats.append(configLines[i].replace("\n"," ").split(','))
    
    for i in range(hatsNum+1, hatsNum + suppliersNum + 1 ):
        suppliers.append(configLines[i].replace("\n"," ").split(','))

    #print(hatsNum)
    #print(hats)
    #print()

    #print(suppliersNum)
    #print(suppliers)


    conn = sqlite3.connect('test_database.db') 
    c = conn.cursor()

    c.execute("DROP TABLE IF EXISTS hats")
    c.execute("DROP TABLE IF EXISTS suppliers")
    c.execute("DROP TABLE IF EXISTS orders")
    conn.commit()

    c.execute('''
          CREATE TABLE IF NOT EXISTS hats
          ([id] INTEGER PRIMARY KEY, [topping] STRING NOT NULL, [supplier] INTEGER REFERENCES Supplier(id), [quantity] INTEGER NOT NULL  )
          ''')
    

    c.execute('''
          CREATE TABLE IF NOT EXISTS suppliers
          ([id] INTEGER PRIMARY KEY, [name] STRING NOT NULL)
          ''')

    c.execute('''
          CREATE TABLE IF NOT EXISTS orders
          ([id] INTEGER PRIMARY KEY, [location] STRING NOT NULL, [hat] INTEGER REFERENCES hats(id) )
          ''')
    
    
    conn.commit()


    for hat in hats:
        c.execute('INSERT INTO hats (id, topping, supplier, quantity) VALUES(?, ?, ?, ?)', (hat[0], hat[1], hat[2], hat[3]))
        

    for supplier in suppliers:
        c.execute('INSERT INTO suppliers (id, name) VALUES(?, ?)', (supplier[0], supplier[1]))

    conn.commit()


    #orderFile = open(args[1], "r")
    orderFile = open("orders.txt", "r")
    orderLines = orderFile.readlines()

    


    #outputFile = open(args[0], "r")
    outputFile = open("output.txt", "w")

    for order in orderLines:
        order = order.replace("\n"," ").split(',')

        #execute order
        c.execute('SELECT b.name FROM hats a INNER JOIN suppliers b ON a.supplier = b.id')

        result = c.fetchall()

        print(order[1] + "," + result[0][0] + "," + order[0])
        


        outputFile.write(order[1] + "," + result[0][0] + "," + order[0]+"\n")
    outputFile.close();


    print("Done just need to write output file")


if __name__ == "__main__":
    main()