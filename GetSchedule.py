import requests

#StudNum = "P23099"
#response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByStudent?osCislo={StudNum}&semestr=1')


result_list = []

"""
faculties = ["FAU","FFI","FPD","FPR","FSS","FZS"]

print(faculties)
for faculty in faculties:
    response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta={faculty}&rok=2023&pouzePlatne=true')
    if response.status_code == 200:
    # Parse the JSON response
       data = response.json()
       oborId_list = [item['oborIdno'] for item in data['oborQRAMInfo']]
       nazevCZ_list = [item['nazevCZ'] for item in data['oborQRAMInfo']]
       nazevEN_list = [item['nazevEN'] for item in data['oborQRAMInfo']]
       #typ = [item['typ'] for item in data['oborQRAMInfo']]
       sublist = list(zip(oborId_list, nazevCZ_list, nazevEN_list, [faculty] * len(oborId_list)))
       result_list.extend(sublist)
"""
#print(result_list)
faculty="FAU"
response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta={faculty}&rok=2023&pouzePlatne=true')
if response.status_code == 200:
    # Parse the JSON response
       data = response.json()
       oborId_list = [item['oborIdno'] for item in data['oborQRAMInfo']]
       nazevCZ_list = [item['nazevCZ'] for item in data['oborQRAMInfo']]
       nazevEN_list = [item['nazevEN'] for item in data['oborQRAMInfo']]
       #typ = [item['typ'] for item in data['oborQRAMInfo']]
       sublist = list(zip(oborId_list, nazevCZ_list, nazevEN_list, [faculty] * len(oborId_list)))
       result_list.extend(sublist)

result2_list = []
for item in result_list:
    oborId, nazevCZ, nazevEN, faculty = item

    for year in range(1,2):
        response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno={oborId}&rocnik={year}&outputFormat=JSON')
        if response.status_code == 200:
    # Parse the JSON response
            data = response.json()
            #print(data)
            
            if data.get('predmetOboru'):
                #print(data)
                zkratka_list = [one["zkratka"] for one in data['predmetOboru']]
                katedra_list = [one["katedra"] for one in data['predmetOboru']]
                nazev_list = [one["nazev"] for one in data['predmetOboru']]
                statut_list = [one["statut"] for one in data['predmetOboru']]
                year_list = [one['rok'] for one in data['predmetOboru']]
                sublist = list(zip([oborId]*len(nazev_list), zkratka_list,katedra_list , nazev_list,statut_list,[nazevCZ]*len(nazev_list), [nazevEN]*len(nazev_list),[faculty] * len(nazev_list), year_list))
                result2_list.extend(sublist)

result3_list = []    
for item in result2_list:
    oborId, zkratka, katedra, nazev, statut, nazevCZ, nazevEN, faculty, year = item
    response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm={zkratka}&pracoviste={katedra}&rokVarianty={year}&outputFormat=JSON')
    if response.status_code == 200:
        data = response.json()
        for akce in data['rozvrhovaAkce']:
            print("data")
            print(data)
            print("akce")
            print(akce)
            if(akce['den']!=None):
                tyden = akce['tyden']
                typHodiny = akce['typAkceZkr']
                den = akce['den']
                hodinaOd = akce['hodinaSkutOd']['value']
                hodinaDo = akce['hodinaSkutDo']['value']
                budova = akce['budova']
                mistnost = akce['mistnost']
                vsichniUciteleJmenaTituly = akce['vsichniUciteleJmenaTituly']
                sublist = [oborId,zkratka, katedra, nazev, statut, nazevCZ, nazevEN, faculty, year, tyden,typHodiny,den,hodinaOd,hodinaDo,budova,mistnost,vsichniUciteleJmenaTituly]
                result3_list.append(sublist)


outputFile = "output2.txt"
with open(outputFile, 'w') as file:
    file.write(str(result3_list))

#response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm=SZZ1&pracoviste=KSZ&rokVarianty=2023')
#print(response.text)   


""" 


outputFile = "output.txt"
with open(outputFile, 'w') as file:
    file.write(str(result2_list))
    

with open("output.txt", 'r') as file:
    result2_list = file.read()
    print(result2_list)

response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm=SZZ1&pracoviste=KSZ&rokVarianty=2023')
print(response.text)   

response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta=FPR&rok=2023&pouzePlatne=true')

print(response.text)

outputFile = "output.txt"

if response.status_code == 200:
    # Parse the JSON response
    data = response.json()
    #oborIdno_list = [item['oborIdno'] for item in data['oborQRAMInfo']]
    print(data)
    #print(oborIdno_list)
    with open(outputFile, 'w') as file:
      file.write(str(data))

selectedOborIdno = "3528"
response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno={selectedOborIdno}&rocnik=1&outputFormat=JSON')


year = "1"
oborIdno = "2717"
response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno={oborIdno}&rocnik={year}&outputFormat=JSON')
print(response.text)

else:
    print("loser")
    """


"""
 // Select one field of study
  const selectedOborIdno = "2717";

  // Second API call
  const secondResponse = await axios.get(`https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno=${selectedOborIdno}&rocnik=1&outputFormat=JSON`);
  const predmety = secondResponse.data.predmetOboru.filter(predmet => predmet.doporucenyRocnik === 1); // Filter by doporucenyRocnik
"""


"""
async def main():
    counter = 0

    # Record start time
    start_time = time.time()

    # The first API call
    # first_response = await axios.get('https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta=FPR&rok=2023&pouzePlatne=true')
    # obory = first_response.data

    # Select one field of study
    selected_obor_idno = "2717"

    # Second API call
    second_response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno={selected_obor_idno}&rocnik=1&outputFormat=JSON')
    predmety = second_response.json()['predmetOboru']
    predmety = [predmet for predmet in predmety if predmet['doporucenyRocnik'] == 1]

    for predmet in predmety:
        zkratka, katedra, rok = predmet['zkratka'], predmet['katedra'], predmet['rok']

        # Third API call
        third_response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm={zkratka}&pracoviste={katedra}&rokVarianty={rok}')
        rozvrhova_akce = third_response.json()['rozvrhovaAkce']

        # Loop through schedule events
        for akce in rozvrhova_akce:
            if akce['den'] is not None:  # Check if 'den' is not null
                counter += 1
                predmet, katedra, typ_akce, den, hodina_od, hodina_do, hodina_skut_od_value, hodina_skut_do_value, vsichni_ucitele_jmena_tituly, mistnost, budova = (
                    akce['predmet'],
                    akce['katedra'],
                    akce['typAkce'],
                    akce['den'],
                    akce['hodinaOd'],
                    akce['hodinaDo'],
                    akce['hodinaSkutOd']['value'],
                    akce['hodinaSkutDo']['value'],
                    akce['vsichniUciteleJmenaTituly'],
                    akce['mistnost'],
                    akce['budova']
                )

                print(f"Zkratka: {predmet}, Katedra: {katedra}, Typ akce: {typ_akce}, Den: {den}, Hodina od: {hodina_od}, Hodina do: {hodina_do}, Hodina skutečně od: {hodina_skut_od_value}, Hodina skutečně do: {hodina_skut_do_value}, Učitel: {vsichni_ucitele_jmena_tituly}, Místnost: {mistnost}, Budova: {budova}")

    # Record end time
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Celková doba trvání: {elapsed_time} sekund")

    print(f"Počet přijatých odpovědí: {counter}")

if __name__ == "__main__":
    import time
    import asyncio

    loop = asyncio.get_event_loop()
    loop.run_until_complete(main())

    """