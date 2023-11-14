import requests

def get_programmes(faculty,year):
    result_list = []
    response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta={faculty}&rok={year}&pouzePlatne=true')
    if response.status_code == 200:
        data = response.json()
        oborId_list = [item['oborIdno'] for item in data['oborQRAMInfo']]
        nazevCZ_list = [item['nazevCZ'] for item in data['oborQRAMInfo']]
        nazevEN_list = [item['nazevEN'] for item in data['oborQRAMInfo']]
        sublist = list(zip(oborId_list, nazevCZ_list, nazevEN_list, [faculty] * len(oborId_list)))
        result_list.extend(sublist)
    return result_list

def get_subjects(programmes):
    result_list = []
    for item in programmes:
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
                    result_list.extend(sublist)
    return result_list

def get_schedule_by_subjects_by_programmes(SubjectsByProgramme):
    result_list = []
    for item in SubjectsByProgramme:
        oborId, zkratka, katedra, nazev, statut, nazevCZ, nazevEN, faculty, year = item
        response = requests.get(f'https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm={zkratka}&pracoviste={katedra}&rokVarianty={year}&outputFormat=JSON')
        if response.status_code == 200:
            data = response.json()
            tyden = [one['tyden'] for one in data ['rozvrhovaAkce']]
            typHodiny = [one['typAkceZkr'] for one in data['rozvrhovaAkce']]
            den = [one['den'] for one in data['rozvrhovaAkce']]
            hodinaOd = [one ['hodinaSkutOd']['value'] for one in data['rozvrhovaAkce']]
            hodinaDo = [one ['hodinaSkutDo']['value'] for one in data['rozvrhovaAkce']]
            budova = [one['budova'] for one in data['rozvrhovaAkce']]
            mistnost = [one['mistnost'] for one in data['rozvrhovaAkce']]
            vsichniUciteleJmenaTituly = [one['vsichniUciteleJmenaTituly'] for one in data['rozvrhovaAkce']]
            sublist = [oborId,zkratka, katedra, nazev, statut, nazevCZ, nazevEN, faculty, year, tyden,typHodiny,den,hodinaOd,hodinaDo,budova,mistnost,vsichniUciteleJmenaTituly]
            result_list.append(sublist)

    result_list_filtered = [sublist for sublist in result_list if None not in sublist[11]]
    result_list_done = [sublist for sublist in result_list_filtered if sublist[0] is not None and sublist[0] != 0 and sublist[9]!=[]]
    return result_list_done

ProgrammesByFaculty = get_programmes("FAU", "2023")
print(ProgrammesByFaculty)
SubjectsByFacultyProgrammes=get_subjects(ProgrammesByFaculty)
print(SubjectsByFacultyProgrammes)
ScheduleBySubjectByFaculty = get_schedule_by_subjects_by_programmes(SubjectsByFacultyProgrammes)
print(ScheduleBySubjectByFaculty)

outputFile = "output2.txt"
with open(outputFile, 'w') as file:
   file.write(str(ScheduleBySubjectByFaculty))