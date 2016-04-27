
import json
import requests
def askWatson(question):
    print question
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
        final_response = {}
        evidences = evidences[:5]
        for e in evidences:
            if len(e.keys()):
                doc_no = e['metadataMap']['DOCNO'].encode("utf-8")
                e_text = e['text'].encode("utf-8")
                title = str(e['metadataMap']['title']).split(' - ')[0]
                final_response[doc_no] ={"text":e_text,"count":1,"title":title}  
    except:
        print("Oops!  Ran into an error.")
        print response.status_code
        return "ERROR:"+str(response.status_code)
    

    print("Watson's answers: ",len(final_response.keys()))
    return final_response


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

