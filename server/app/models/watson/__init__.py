
import json
import requests

def askWatson(question):
    data={"question": {"questionText" : question}}

    username="gt2_administrator"
    password="bCze2OdC"

    url="https://watson-wdc01.ihost.com/instance/514/deepqa/v1/question"
    headers={"Content-type": "application/json",
             "Accept": "application/json",
             "X-SyncTimeout": "30"}

    print("Asking Watson: " + question + "\n")


    try:

        response = requests.post(url, data=json.dumps(data), auth=(username, password), headers=headers)

        # Print out the best answer
        jsonResponse = response.json()
        evidences = jsonResponse["question"]["evidencelist"]

    except:
        
        print("Oops!  Ran into an error.")
        print response.status_code
        return "ERROR:"+str(response.status_code)
        
    # jsonResponse = response.json()

    if('text' not in evidences[0]):
        print 'Answer not found'
        return "Answer not found"

    firstAnswer = evidences[0]["text"]

    print("Watson's answer: " + firstAnswer)
    return firstAnswer


def main():

    print("===== Starting ======\n\n")

    while True:
        user_input = raw_input("Enter a Question or Exit: ")
        if user_input == "Exit":
            break
        else:
            print("\n")
            askWatson(user_input)
            print("\n\n")

    print("\n\n===== Exiting ======")

if __name__ == '__main__':
    main()

