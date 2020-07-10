package nl.jovmit.lyrics.main

import com.google.firebase.firestore.FirebaseFirestore
import nl.jovmit.lyrics.common.AppCoroutineDispatchers
import nl.jovmit.lyrics.common.CoroutineDispatchers
import nl.jovmit.lyrics.main.add.NewSongRepository
import nl.jovmit.lyrics.main.add.NewSongViewModel
import nl.jovmit.lyrics.main.data.song.*
import nl.jovmit.lyrics.main.details.SongDetailsViewModel
import nl.jovmit.lyrics.main.edit.UpdateSongViewModel
import nl.jovmit.lyrics.main.overview.SongsRepository
import nl.jovmit.lyrics.main.overview.SongsViewModel
import nl.jovmit.lyrics.utils.IdGenerator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { FirebaseFirestore.getInstance() }
    single<CoroutineDispatchers> { AppCoroutineDispatchers() }
    single { IdGenerator() }
//    single<SongsService> { FirebaseSongsService(get()) }
    single<SongsService> { InMemorySongsService(IdGenerator(), generateSongs()) }
    factory { SongsRepository(get()) }
    factory { NewSongRepository(get()) }
    viewModel { SongsViewModel(get(), get()) }
    viewModel { NewSongViewModel(get(), get()) }
    viewModel { InfoViewModel() }
    viewModel { SongDetailsViewModel(get(), get()) }
    viewModel { UpdateSongViewModel(get(), get()) }
}

fun generateSongs(): List<Song> {
    return listOf(
        Song(
            SongId("1"),
            SongTitle("Пожар"),
            SongPerformer("Новица Василевски"),
            SongLyrics("Прво ме гледаш а после бегаш\nбиди ми среќа доста сакам неќам\n\nти жарче јас оган\nќе гори леле силен пожар\nти жарче јас оган \nпожар ќе гори леле дур до зори\n\nво тебе нешто се крие жешко\nвеќе ти пеам само да се згреам\n\nпоглед ко срна јас веќе тргнав\nи се ќе згазам тебе да те мазам")
        ),
        Song(
            SongId("2"),
            SongTitle("Јана убава"),
            SongPerformer("Народна"),
            SongLyrics("Дотекла вода студена\nроди се Јана убава\nдотекла вода студена\nроди се Јана убава\n\nкога се Јана родила\nмајка и маѓија сторила\nкога се Јана родила\nмајка и маѓија сторила\n\nмајка и маѓија сторила\nживо пиле воган фрлила\nмајка и маѓија сторила\nживо пиле воган фрлила\n\nкако што се врти пилето\nтака да се вртат ергени\nпо нашта Јана убава")
        ),
        Song(
            SongId("3"),
            SongTitle("Абре воденичаре"),
            SongPerformer("Народна"),
            SongLyrics("Абре воденичаре баш пријателе\nморе мојве очи твои нека бидат\nсомелиго житото\n\nабре невесте око калешо\nводеницата вода ми нема\nне ти мелам житото\n\nабре воденичаре баш пријателе\nморе мојво лице твое нека биде\nсомелиго житото\n\nабре невесте око калешо\nводеницата камен ми нема\nне ти мелам житото\n\nабре воденичаре баш пријателе\nморе мојва уста твоја нека биде\nсомелиго житото\n\nабре невесте око калешо\nводеницата бука ми нема\nне ти мелам житото\n\nабре воденичаре баш пријателе\nморе мојва снага твоја нека биде\nсомелиго житото\n\nабре невесте око калешо\nштом така велиш така нека биде\nќе ти мелам житото\n")
        ),
        Song(
            SongId("4"),
            SongTitle("Памтиш ли либе Тодоро"),
            SongPerformer("Народна"),
            SongLyrics("Памтиш ли либе Тодоро галабе\nпамтиш ли либе Тодоро\nкога си бевме малечки галабе\nкога се двајца љубевме\n\nкога си бевме малечки галабе\nкога се двајца љубевме\nво чичовото градинче галабе\nпод едно дрво маслинче\n\nна едно гранче стоевме галабе\nне едно гранче стоевме\nлоша си клетва зедовме галабе\nлоша си клетва зедовме\nна едно гранче стоевме галабе\nлоша си клетва зедовме\n\nкој ќе се први ожени галабе\nкој ќе се први ожени\nтој тешко болен да лежи галабе\nтој тешко болен да лежи\nкој ќе се први ожени галабе\nтој тешко болен да лежи")
        ),
        Song(
            SongId("5"),
            SongTitle("Девојче танко високо"),
            SongPerformer("Народна"),
            SongLyrics("Девојче танко високо\nдевојче танко високо високо\nвиско високо\nне оди ситно пред мене\nне оди ситно пред мене задмене\nпред мене зад мене\n\nне дигај магли праови\nне дигај магли праови праови\nпраови праови\nне ми задавај јадови\nне ми задавај јадови јадови\nјадови јадови\n\nмалку ли ми се моите\nмалку ли ми се моите моите\nмоите моите\nкај да ги ставам твоите\nкај да ги ставам твоите твоите\nтвоите твоите")
        ),
        Song(
            SongId("6"),
            SongTitle("Денеска ми е сабота"),
            SongPerformer("Народна"),
            SongLyrics("Денеска ми е сабота оф аман\nКираца ќоди на пазар оф аман\nќе ми го бара Наќета\nќе ми го бара Наќета оф аман\nНаќета ќирајџијата оф аман\nНаќета ќирајџијата\n\nНаќе ле Наќе браче ле оф аман\nкога ќе одиш во влашко оф аман\nи мене абер да сториш\nи мене абер да сториш оф аман\nи јас ќе дојдам со тебе оф аман\nколку ќирија ќе бараш\n\nКирацо млада невесто оф аман\nкирија ти е ефтина оф аман\nод тебе пари не земам\nдо Скопје белото лице оф аман\nдо Белград медното усте оф аман\nдо влашко рамната снага")
        ),
        Song(
            SongId("7"),
            SongTitle("Вело ќерко"),
            SongPerformer("Влатко Миладиноски"),
            SongLyrics("Ај стани Вело ќерко, ај стани\nај ја стани Вело ќерко, порано\nда месиш Вело ќерко, да месиш\nај да месиш Вело ќерко, девет погачи\n\nќе дојдат Вело ќерко, ќе дојдат\nај ќе дојдат Вело ќерко, селски сејмени\nда спремиш Вело ќерко, да спремиш\nај да спремиш Вело ќерко, бели дарови\n\nќе дојдат Вело ќерко, ќе дојдат\nај ќе дојдат Вело ќерко, тебе да земат\nќе дојдат Вело ќерко, ќе дојдат\nај ќе дојдат Вело ќерко тебе да земат")
        ),
        Song(
            SongId("8"),
            SongTitle("Ајде вино пијам"),
            SongPerformer("Народна"),
            SongLyrics("Ајде вино пијам леле вино пијам\nнепара го пијам леле леле\nнепара го пијам\nајде на ракија леле на ракија\nмногу сум мераклија леле леле\nмногу сум мераклија\n\nајде коња јавам леле коња јавам\nкоња аџамија леле леле\nкоња аџамија\nајде јас го носам леле јас го носам\nна шарен кладенец леле леле\nна шарен кладенец\n\nајде тој ме носи  тој ме носи\nпред момини порти леле леле\nпред момини порти\nајде тешки порти тешки порти\nбеја затворени леле леле\nбеја затворени\n\nајде не сум пиле не сум пиле\nпорти да прелетам леле леле\nпорти да прелетам\nајде не сум змија не сум змија\nпод порти да влезам леле леле\nпод порти да влезам\nајде тук сум јунак тук сум јунак\nпорти да искршам леле леле\nпорти да искршам")
        ),
        Song(
            SongId("9"),
            SongTitle("Песна за Исус Христос"),
            SongPerformer("Народна"),
            SongLyrics("Од кај што сонце изгрева ааа..\nдури до кај што зајдува ааа...\nзад високите планини\nзад високите планини ааа ...\nрајска трпеза ставена\n\nна трпеза ми седеја ааа...\nсите ми божји ангели\nна среде свети никола ааа...\nдо него света марија ааа...\nв раце го држи ристоса\n\nдувнале силни ветрови ааа...\nристоса ми го зедоја ааа...\nписна марија да плачи\nписна марија да плачи ааа...\nвратете ми го ристоса\n\nмолчи марие не плачи ааа...\nако го ристос зедоја ааа...\nзанает ќе го научат\nзанает ќе го научат ааа...\nстрашни мостови да прави\nгрешните луѓе да врват ааа...\nи по нив пијаниците")
        ),
        Song(
            SongId("10"),
            SongTitle("Јоване море Јоване"),
            SongPerformer("Народна"),
            SongLyrics("Јоване море Јоване\nчаршија збори за тебе\nоти си добро мајсторче\nмајсторче арно терзиче\n\nеве ти свила коприна\nфустанот да ми сошиеш\nако ми згрешиш мерката\nдуќанот ќе ти запалам\nсо него ти да изгориш\nи твојте лични чираци\n\nкоконо мори коконо\nкоконо лична убава\nмерката ти е земена\nуште пред две три недели\n\nкога од бања идеше\nсенка во дуќан паѓаше\nтогаш ти мерка зедовме")
        ),
        Song(
            SongId("11"),
            SongTitle("Еднаш мајка јунак раѓа"),
            SongPerformer("Народна"),
            SongLyrics("Ајде да си речиме една песна\nмногу гласовита\n\nеднаш мајка јунак раѓа\nоној ми е билбил\nбилбил рано пее\nпее во мај месец\n\nајде да си речиме уште една \nда се сторат два пати\nдве очи мома има\n\nајде да си речиме уште една\nда се сторат три пати\nтри накрст пирустија\nдве очи мома има\n\nајде да си речиме уште една\nда се сторат четири пати\nчетири моми на орото\nтри накрст пирустија\nдве очи мома има\n\nајде да си речиме уште една\nда се сторат пет пати\nпет прсти рака има\nчетири моми на орото\nтри накрст пирустија \nдве очи мома има\n\nајде да си речиме уште една\nда се сторат шест пати\nшест месеци пол година\nпет прсти рака има\nчетири моми на орото\nтри накрст пирустија\nдве очи мома има\n\nајде да си речиме уште една \nда се сторат седум пати\nседум дена во недела\nшест месеци пол година\nпет прсти рака има\nчетири моми на орото\nтри накрст пирустија\nдве очи мома има")
        ),
        Song(
            SongId("12"),
            SongTitle("Фросино моме убаво"),
            SongPerformer("Народна"),
            SongLyrics("Фросино моме убаво\nај покри си белото лице\nсо свиленото шамивче\nда не те види пашата\nод високите чардаци\nсо шарената дурбија\n\nуште зборот не доречен\nпашата ми ја здогледа\nчаскум ми пратил гавази\nна Фросина и велат говорат\nселам ти чини алипаша\n\nФросино моме убаво\nселам ти чини алипаша\nвечер вечера да зготвиш\nво шарената одаја\nна џам власите миндери\nпашата да го пречекаш\nсо дванаесет сејмени\n\nФросина вели говори\nгавази море гавази\nздраво живо на алипаша\nда не се лажи да дојди\nјас сум си моме рисјанче\nјас вера не си менувам\nза еден турчин алипаша")
        ),
        Song(
            SongId("13"),
            SongTitle("Песна за Александар Турунџев"),
            SongPerformer("Народна"),
            SongLyrics("Црно му било пишано\nна александро Турунџев\nшеснајс годишен војвода\nна седумнајста го фатија\n\nи го турија во зандани\nво тие тесни долапи\nсо тешки пранги на нозе\nчифт белегзии на раце\nем дробен синџир на гуша\n\nтелеграм дојде од стамбол\nза алксандро Турунџев\nда си ја види невестата\nем својте дробни дечиња\n\nго излагаа александро\nму ја кажаа бесилката\nеве ти ја невестата\nем твојте дробни дечиња\n\nкажи бре кажи александро\nшто лошо ти си направил\nво твој шеснајсет години\n\nништо лошо не сум сторил\nанките ви ги одбулив\nдецата ви ги прекрстив\n\nБитола го однесоа\nат пазар го обесија\nцела Битола се расплака\nза александро Турунџев\nза алксандро Турунџев\nшеснајс годишен војвода\n")
        ),
        Song(
            SongId("14"),
            SongTitle("Ах мори бабо"),
            SongPerformer("Народна"),
            SongLyrics("Ах мори бабо стара бабо\nај што имаш ќерка убава \nбабо аман аман\n\nај што имаш ќерка убава \nај што имаш лоши комшии\nбабо аман аман \n\nај што има добри сватови\nај што добри зборој фрлаат\nбабо аман аман\n\nај што добри зборој фрлаат\nкако на морето далгите\nбабо аман аман\nкако на небото ѕвездите\nбабо аман аман")
        ),
        Song(
            SongId("15"),
            SongTitle("Австралија земја печалбарска"),
            SongPerformer("Народна"),
            SongLyrics("Бог да го бие и да го убие\nтој што ја пронајде\nАвстралија земја печалбарска\nем океанска\n\nмногу печалбари таму заминаја\nпари спечалија\nпари спечалија назад се вратија\nсо роднини се видоја\n\nмногу време мина откако замина\nи моето либе\nниту пари праќа ни назад се враќа\nниту мене таму ме зема\n\nна рака ми лежат двете машки деца\nжално расплакани\nкажи мајко што си му згрешила\nна нашиот татко\nтебе што те остави а нас не заборави\nоф леле изгор за него\n\nако сум згрешила мили мои деца\nпроклета да бидам\nпроклета да бидам на оваа црна земја\nсо мојта црна судбина")
        ),
        Song(
            SongId("16"),
            SongTitle("Сека вечер пијан се враќам"),
            SongPerformer("Народна"),
            SongLyrics("Сека вечер пијан се враќам\nееј севдо ле Севдалино\n\nтаа вечер трезен се вратив\nееј севдо ле Севдалино\n\nтаа вечер севда ја нема\nееј севдо ле Севдалино")
        ),
        Song(
            SongId("17"),
            SongTitle("Бог да ги бие дебрани"),
            SongPerformer("Народна"),
            SongLyrics("Бог да ги бие дебрани\nдебрани горно граѓани\n\nоделе што ми оделе\nпо таа река радика\nпо тие села рисјански\n\nграбиле што ми грабиле\nи едно невевче земале\n\nводиле што ми водиле\nмладото невевче потстана\n\nневевче лично калешо\nдали ти тежи рувото\nили ти тежи ѓерданот\n\nнити ми тежи рувото\nнити ми тежи ѓерданот\n\nтук ми е жал за лудото\nкога го вие колевте\n\nправо во мене гледаше\nкрвави солзи ронеше")
        ),
        Song(
            SongId("18"),
            SongTitle("Есен дојде"),
            SongPerformer("Народна"),
            SongLyrics("Есен дојде во нашиот крај\nво армија војници одат\n\nмоето либе со нив појде\nво туѓина во непознат крај\n\nсолза по солза поток потече\nиздив по издив ветар завеа\n\nјас останав дома да седам \nда го чекам до година\n\nесен дојде во нашиот крај\nод армија војници идат\n\nмоето либе со нив дојде\nод туѓина од непознат крај")
        ),
        Song(
            SongId("19"),
            SongTitle("Хусо темјанички"),
            SongPerformer("Народна"),
            SongLyrics("Карала го мајка хусо темјанички \nхусо темјанички царот неготински\nај послушај хусо старата си мајка\nнемој не ми оди на каурска свадба\n\nне ми оди хусо на каурска свадба\nкаури се лоши ќе се поднапијат\nкаури се лоши ќе се поднапијат\nќе се поднапијат ќе ми те убијат\n\nхусо не послуша старата си мајка\nпа ми е отишол на каурска свадба\nкаурите лоши ми се поднапиле\nми се поднапиле хусо го убиле\n\nдодо додо додо путето му нанино\nна хусо темјанички царот неготински\nзошто не послуша старата си мајка\nпа ми е отишол на каурска свадба")
        ),
        Song(
            SongId("20"),
            SongTitle("Три години стана"),
            SongPerformer("Народна"),
            SongLyrics("Три години стана како ме зафана лудо да ме мачи твојата љубов\n\nах колку е мачно ах колку е жално\nјас да те љубам друг да те земи\nја земи ножето распарај срцето\nтаму ќе си најдеш искрена љубов\n\nкаква мајка беше тбе што те роди\nцел свет да се чуди на твојта убост\nмојта мајка беше лична симпатична\nи мене ме роди уште полична\n\nако не те земам млада убавица\nјас ќе те земам црна вдовица\nако не те земам долу на земјата\nјас ќе те земем горе на небо")
        )
    )
}