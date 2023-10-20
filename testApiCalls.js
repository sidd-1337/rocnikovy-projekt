const axios = require('axios');

async function main() {
  let counter = 0;

  // Record start time
  const startTime = Date.now();

  // The first API call 
  /* const firstResponse = await axios.get('https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta=FPR&rok=2023&pouzePlatne=true');
  const obory = firstResponse.data; */

  // Select one field of study
  const selectedOborIdno = "2717";

  // Second API call
  const secondResponse = await axios.get(`https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno=${selectedOborIdno}&rocnik=1&outputFormat=JSON`);
  const predmety = secondResponse.data.predmetOboru.filter(predmet => predmet.doporucenyRocnik === 1); // Filter by doporucenyRocnik

  for (const predmet of predmety) {
    const { zkratka, katedra, rok } = predmet;

    // Third API call
    const thirdResponse = await axios.get(`https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm=${zkratka}&pracoviste=${katedra}&rokVarianty=${rok}`);
    const rozvrhovaAkce = thirdResponse.data.rozvrhovaAkce;

    // Loop through schedule events
    for (const akce of rozvrhovaAkce) {
      if (akce.den !== null) {  // Check if 'den' is not null
        counter++;
        const {
          predmet,
          katedra,
          typAkce,
          den,
          hodinaOd,
          hodinaDo,
          hodinaSkutOd: { value: hodinaSkutOdValue },
          hodinaSkutDo: { value: hodinaSkutDoValue },
          vsichniUciteleJmenaTituly,
          mistnost,
          budova
        } = akce;

        console.log(`Zkratka: ${predmet}, Katedra: ${katedra}, Typ akce: ${typAkce}, Den: ${den}, Hodina od: ${hodinaOd}, Hodina do: ${hodinaDo}, Hodina skutečně od: ${hodinaSkutOdValue}, Hodina skutečně do: ${hodinaSkutDoValue}, Učitel: ${vsichniUciteleJmenaTituly}, Místnost: ${mistnost}, Budova: ${budova}`);
      }
    }
  }

  // Record end time
  const endTime = Date.now();
  const elapsedTime = (endTime - startTime) / 1000;
  console.log(`Celková doba trvání: ${elapsedTime} sekund`);

  console.log(`Počet přijatých odpovědí: ${counter}`);
}

main().catch(err => console.error(err));
