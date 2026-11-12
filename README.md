# Лаборатори 10 — Өгөгдлийн урсгалын тест

О. Шинэбаяр — **B232270023**  
F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест

AI туслахыг зааврыг тулгах, DU хосын шинжилгээ, JUnit тест, CFG болон тайлан боловсруулахад ашиглав. Эх кодын `DataFlowTargets.java`-г зөвхөн fault seeding хэсэгт түр мутацилж, туршилт бүрийн дараа яг хэвээр нь буцаасан.

## Орчин

`java -version`:

```text
openjdk version "17.0.20.1" 2026-08-18
OpenJDK Runtime Environment Temurin-17.0.20.1+1 (build 17.0.20.1+1)
OpenJDK 64-Bit Server VM Temurin-17.0.20.1+1 (build 17.0.20.1+1, mixed mode, sharing)
```

`mvn -version` (Apache Maven-ийг workspace-д задлан ажиллуулав):

```text
Access is denied.
Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
Maven home: C:\Users\newtech\Documents\ChatGPT\Turshilt\lab10-tools\apache-maven-3.10.0
Java version: 17.0.20.1, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot
Default locale: en_US, platform encoding: Cp1252, time zone: Asia/Ulaanbaatar
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```

## A. `gcd` — DU шинжилгээ

Доорх мөрийн дугаарууд `src/main/java/mn/edu/must/sqat/DataFlowTargets.java`-ийн мөр. `13` дэх параметрийн утгууд definition; `14` дэх `int tmp;` нь эхлүүлээгүй зарлал тул definition биш. c-use нь илэрхийлэл/буцаах утгад, p-use нь нөхцөлд хэрэглэгдэнэ. Мөр тус бүр нь def болон use байрлалын хосыг харуулна.

| ID | Хувьсагч | Def мөр | Use мөр | Төрөл | Def-clear зам | Тест | Хамрагдсан |
|---|---|---:|---:|---|---|---|---|
| GDU01 | `x` | 13 | 16 | c-use | 13→15T→16 | G2 `twoIterations` | Тийм |
| GDU02 | `x` | 13 | 20 | c-use | 13→15F→20 | G1 `zeroIterations` | Тийм |
| GDU03 | `x` | 17 | 16 | c-use | 17→18→15T→16 | G2 `twoIterations` | Тийм |
| GDU04 | `x` | 17 | 20 | c-use | 17→18→15F→20 | G2 `twoIterations` | Тийм |
| GDU05 | `y` | 13 | 15 | p-use T/F | 13→15T; 13→15F | G2 T; G1 F | Тийм |
| GDU06 | `y` | 13 | 16 | c-use | 13→15T→16 | G2 `twoIterations` | Тийм |
| GDU07 | `y` | 13 | 17 | c-use | 13→15T→16→17 | G2 `twoIterations` | Тийм |
| GDU08 | `y` | 18 | 15 | p-use T/F | 18→15T; 18→15F | G2 хоёр дахь/төгсгөлийн шалгалт | Тийм |
| GDU09 | `y` | 18 | 16 | c-use | 18→15T→16 | G2 `twoIterations` | Тийм |
| GDU10 | `y` | 18 | 17 | c-use | 18→15T→16→17 | G2 `twoIterations` | Тийм |
| GDU11 | `tmp` | 16 | 18 | c-use | 16→17→18 | G2 `twoIterations` | Тийм |

`G1 zeroIterations` нь `gcd(7, 0) = 7`; давталтыг алгасаж `x:13→20`-ийг дангаараа хамруулна. Энэ хос давталтад орсон гүйцэтгэлд боломжгүй, учир нь `x`-ийг 17-р мөрд дахин тодорхойлно. `G2 twoIterations` нь `gcd(12, 8) = 4`; дараалал `(12,8)→(8,4)→(4,0)` тул хоёр удаа давтана. G2 бүх давталтын хос, мөн сүүлчийн `15F` ба `20`-р мөрийн хэрэглээг хамарна. Ингэж 11 def–use байрлалын хос, p-use-ийн хоёр салааг тусад нь тооцвол нийт 13 шалгалтыг хамруулав.

## B. `bonus` — анализ ба туршилт

Үр дүнг all-defs, all-DU-pairs, fault seeding гэсэн дараалсан үе шат болон тусдаа commit-уудаар баримтжуулна.

### B1–B3. CFG, def/use жагсаалт, all-DU-pairs

`cfg-bonus.png` дахь CFG нь for-each-ийн 38-р мөрийг `attendance`-ийн дараагийн элементийн p-use, алхмыг `a`-ийн шинэ definition гэж үзүүлэв. Цэнхэр ногоон ирмэгийн тайлбарт тухайн урсгалыг бодитоор дайрах тестийн методын нэрийг бичив.

| Мөр | Definition | Use |
|---:|---|---|
| 31 | `attendance`, `threshold` параметр | — |
| 32 | — | `attendance == null` p-use; attendance null биш үед `threshold < 0` p-use |
| 35 | `present = 0` | — |
| 36 | `streak = 0` | — |
| 37 | `best = 0` | — |
| 38 | `a`-д дараагийн элементийг онооно | `attendance`-д дараагийн элемент байгаа эсэх p-use |
| 39 | — | `a > 0` p-use |
| 40 | `present = present + 1` | `present` c-use |
| 41 | `streak = streak + 1` | `streak` c-use |
| 42 | — | `streak > best`: `streak`, `best` p-use |
| 43 | `best = streak` | `streak` c-use |
| 46 | `streak = 0` | — |
| 49 | `score = 0` | — |
| 50 | — | `present >= threshold`: хоёул p-use |
| 51 | `score = 5` | — |
| 53 | — | `best >= 5` p-use |
| 54 | `score = score + 3` | өмнөх `score` c-use |
| 56 | — | `score` c-use |

| ID | Хувьсагч | Def→Use | Төрөл, def-clear зам | Хамрах тест | Хамрагдсан |
|---|---|---|---|---|
| BDU01 | attendance | 31→32 | p-use T/F; 31→32 | `nullAttendance`; `allDefsMixedRun` | Тийм |
| BDU02 | attendance | 31→38 | p-use T/F; 31→32F→35→36→37→38T/F | `allDefsMixedRun`; `allDefsEmpty` | Тийм |
| BDU03 | threshold | 31→32 | p-use T/F; 31→32(attendance F)→32(threshold T/F) | `negativeThreshold`; `allDefsMixedRun` | Тийм |
| BDU04 | threshold | 31→50 | p-use T/F; 31→32F→35→36→37→38(T/loop/F)→49→50T/F | `allDefsMixedRun`; `allDefsEmpty` | Тийм |
| BDU05 | present | 35→40 | c-use; 35→36→37→38T→39T→40 | `singleDayAtThreshold`; `allDefsMixedRun` | Тийм |
| BDU06 | present | 35→50 | p-use; 35→36→37→38F→49→50 | `allDefsEmpty`; `allAbsentZeroThreshold` | Тийм |
| BDU07 | present | 40→40 | c-use; 40→41→42→38T→39T→40 | `allDefsMixedRun`; `fiveDaysBelowThreshold` | Тийм |
| BDU08 | present | 40→50 | p-use; 40→41→42→38(loop/exit)→49→50 | `allDefsMixedRun`; `fiveDaysBelowThreshold` | Тийм |
| BDU09 | streak | 36→41 | c-use; 36→37→38T→39T→40→41 | `singleDayAtThreshold`; `allDefsMixedRun` | Тийм |
| BDU10 | streak | 41→41 | c-use; 41→42(T→43/F)→38T→39T→40→41 | `allDefsMixedRun`; `fiveDaysBelowThreshold` | Тийм |
| BDU11 | streak | 41→42 | p-use T/F; 41→42T/F | `allDefsMixedRun` | Тийм |
| BDU12 | streak | 41→43 | c-use; 41→42T→43 | `allDefsMixedRun`; `fiveDaysBelowThreshold` | Тийм |
| BDU13 | streak | 46→41 | c-use; 46→38T→39T→40→41 | `allDefsMixedRun` | Тийм |
| BDU14 | best | 37→42 | p-use T; 37→38T→39T→40→41→42T | `singleDayAtThreshold`; `allDefsMixedRun` | Тийм |
| BDU15 | best | 37→53 | p-use F; 37→38F→49→50→53F | `allDefsEmpty`; `allAbsentZeroThreshold` | Тийм |
| BDU16 | best | 43→42 | p-use T/F; 43→38T→39T→40→41→42T/F | `allDefsMixedRun` | Тийм |
| BDU17 | best | 43→53 | p-use T/F; 43→(43-р мөрөөр дахин тодорхойлолтгүй алхмууд)→38F→49→50→53T/F | `allDefsMixedRun`; `singleDayAtThreshold`; `fiveDaysBelowThreshold` | Тийм |
| BDU18 | a | 38→39 | p-use T/F; 38T→39T/F | `allDefsMixedRun`; `allAbsentZeroThreshold` | Тийм |
| BDU19 | score | 49→54 | c-use; 49→50F→53T→54 | `fiveDaysBelowThreshold` | Тийм |
| BDU20 | score | 49→56 | c-use; 49→50F→53F→56 | `allDefsEmpty` | Тийм |
| BDU21 | score | 51→54 | c-use; 51→53T→54 | `allDefsMixedRun` | Тийм |
| BDU22 | score | 51→56 | c-use; 51→53F→56 | `singleDayAtThreshold`; `allAbsentZeroThreshold` | Тийм |
| BDU23 | score | 54→56 | c-use; 54→56 | `allDefsMixedRun`; `fiveDaysBelowThreshold` | Тийм |

46-р мөрийн `streak = 0` нь өмнөх `streak`-ийн definition-үүдийг kill хийнэ (36 эсвэл 41-р мөрөөс ирсэн утга). Мөн 46-р мөр өөрөө шинэ `streak` definition. Энэ нь `present` болон `best`-ийг kill хийхгүй. `streak:36→42` болон `streak:46→42` def-clear хос биш: нөхцөл 42 хүрэхээс өмнө дараагийн эерэг өдөр 41-р мөрд `streak`-ийг дахин тодорхойлно. `best:37→42`-ын F салаа биелэхгүй: анхны эерэг өдөр `streak` хамгийн багадаа 1, `best` 0 тул нөхцөл үнэн; харин эерэг өдөр байхгүй бол 42-р мөрд хүрэхгүй. `best:37→53`-ын T салаа ч биелэхгүй: `best >= 5` болох бүх зам 43-р мөрийн шинэ definition-ээр 37-р мөрийн утгыг kill хийнэ.

`B1 allDefsMixedRun`, `B2 allDefsEmpty`, `B3 singleDayAtThreshold`, `B4 fiveDaysBelowThreshold`, `B5 nullAttendance`, `B6 negativeThreshold`, `B7 allAbsentZeroThreshold` гэсэн 7 bonus тест хүснэгтийн боломжтой бүх хос/салааг хамарлаа. `results/mvn-verify-alldupairs.txt`-д нийт 9 тест ногоон; `results/jacoco-alldupairs.csv`-д `DataFlowTargets` branch 16/16, line 23/24. Үлдсэн ганц мөр нь 7-р мөрийн implicit constructor; үүнийг хамруулахын тулд объект үүсгэх нэмэлт тест бичих шаардлагагүй.

### B4. All-defs туршилтын шат

`BonusTest`-ийн эхний хоёр тест all-defs-ийг хангана. `B1 allDefsMixedRun`-д `attendance={1,0,1,1,1,1,1}`, `threshold=6`; ирсэн өдөр 6, хамгийн урт дараалал 5 тул оноо 8. Энэ зам параметрүүд болон `present`, `streak`, `best`, `a`, `score`-ийн бүх definition-ээс дор хаяж нэг use-д хүрнэ. `B2 allDefsEmpty`-д `attendance={}`, `threshold=1`; хоосон давталтын дараах анхны `present`, `best`, `score` definition-үүд хэрэг болно.

Bonus-ийн all-defs-д **2 тест зайлшгүй**. `score`-ийн 49-р мөрийн definition хэрэглэхийн тулд `present >= threshold` нөхцөл худал байх ёстой. Үнэн бол 51-р мөр `score`-ийг use-д хүрэхээс өмнө дахин тодорхойлно. Харин 51-р мөрийн definition-ийг use-д хүргэхэд уг нөхцөл үнэн байх шаардлагатай. Нөхцөл давталтын дараа нэг удаа л шалгагддаг тул нэг дуудлага хоёр score definition-ийг хоёуланг нь use-д хүргэх боломжгүй. Хоёр тестээр бусад бүх definition-ийг мөн use-д хүргэсэн.

`results/mvn-verify-alldefs.txt`-д нийт 4 тест амжилттай (gcd 2, bonus 2) гэж гарсан. `results/jacoco-alldefs.csv`-ийн `DataFlowTargets` мөр: branch 14/16 (2 missed), line 22/24 (2 missed). All-defs нь branch-ийн 100%-ийг батлахгүй: энэ шат `attendance == null` болон `threshold < 0` оролтын шалгалтын үнэн салааг хамраагүй тул хоёр branch дутсан.

Branch хамралт 100% болсон ч DU хүснэгтэд хос үлдэж болно. Үүнийг `results/mvn-verify-branch-example.txt`-ийн сонгосон 6 тест (gcd 2, `allDefsMixedRun`, `allDefsEmpty`, `nullAttendance`, `negativeThreshold`) харуулна: `results/jacoco-branch-example.csv`-д branch 16/16 боловч BDU19 (`score:49→54`) огт хүрээгүй. Энэ хос зөвхөн `present < threshold` ба `best >= 5` нэг дор биелэхэд хэрэгжинэ; сонгосон тестүүдэд тэр нөхцөл байхгүй. Иймээс JaCoCo branch-ийн 100% нь хувьсагчийн def-clear зам бүрийг шалгасны нотолгоо биш.
### B5. Эцсийн хэмжилт

`results/mvn-verify-final.txt` нь `mvn verify`-ийн эцсийн гаралт: 10 тест амжилттай, алдаа/унасан тест 0, build success. `results/jacoco-final.csv`-ийн `DataFlowTargets` мөрд branch 16/16 (`BRANCH_MISSED=0`), line 23/24 (`LINE_MISSED=1`) хамрагдсан. Missed ганц мөр нь 7-р мөрийн далд constructor. Алдаа тарих үед өөрчилсөн `src/main` файлыг буцаасан тул тайлан болон эх кодын хувилбар хоорондоо таарна.

## C. Fault seeding

Мутаци бүрийг ганцаар нь `DataFlowTargets.java` дээр хийж `mvn test` ажиллуулаад, дараа нь файлыг `git restore`-оор сэргээв. Үр дүнгүүд:

| Мутаци | Тестийн үр дүн | Дүгнэлт |
|---|---|---|
| M1 `streak = streak + 1`-ийг устгах | 9 тестээс 2 унав. `allDefsMixedRun`: expected 8, was 5; `fiveDaysBelowThreshold`: expected 3, was 0. | Дутуу update-ийг DU тестүүд илрүүлэв. Бүрэн лог: `results/mvn-test-M1.txt`. |
| M2 `best = streak` → `best = present` | Эхний all-DU-pairs багцын 9 тест бүгд тэнцэв. | `allDefsMixedRun`-д тасалсны дараах update-уудын үед `(present, streak)` нь (3,2), дараа нь (5,4); буруу `best=present` утгууд 3 ба 5 өгсөн ч эцсийн `best >= 5` салаа зөв хувилбарын адил үнэн тул гаралт 8 хэвээр. `singleDayAtThreshold` ба `fiveDaysBelowThreshold`-д best шинэчлэгдэх бүрд `present=streak`; хоосон болон бүх таслалтын тестэд best шинэчлэгдэхгүй. Бүрэн лог: `results/mvn-test-M2.txt`. |
| M2-ийн нэмэлт шалгалт | `separatedRunsDoNotEarnStreakBonus`-г нэмсний дараах 10 тестээс тэр тест expected 5, was 8 гэж унав. | `present=5`, хамгийн урт `streak=3`; буруу хувилбар 3-аас ихгүй streak-ийн оронд `present`-ийг `best` болгодог. Лог: `results/mvn-test-M2-kill.txt`. |
| M3 `int total = present + best;` нэмэх | 10 тест бүгд тэнцэв. | Dead definition ажиглагдах үр дүн өөрчлөхгүй тул unit test барихгүй. `javac` ч алдаа/unused warning гаргаагүй. |

M3-ийг static analysis илрүүлнэ. PMD `UnusedLocalVariable` дүрмийн тайлан `results/pmd-M3.xml`-д энэ зөрчлийг ингэж тэмдэглэсэн: **“Avoid unused local variables such as 'total'.”** Maven-ийн шинжилгээний лог `results/static-analysis-M3.txt`-д байна. Туршилтын дараа `git diff -- src/main/java/mn/edu/must/sqat/DataFlowTargets.java` хоосон; эцсийн source starter-тэй ижил.


## Дүгнэлт

DU анализ нь хувьсагчийн тодорхойлолт аль хэрэглээнд хүрч байгааг тодорхой харуулдаг. Branch хамралт нь шийдвэрийн салааг тоолдог ч аль definition аль use-д хүрснийг хэлдэггүй. All-defs нь definition бүрийн дор хаяж нэг хэрэглээг шалгадаг бөгөөд бүх def-use хосыг батлахгүй. All-DU-pairs хүснэгт илүү нарийн нотолгоо өгдөг ч зам, давталтын kill болон биелэшгүй хосыг гараар ялгах цаг шаарддаг. `best = present` мутаци бүх эхний DU тестийг давсан нь хосын хамралт дангаараа зөв үр дүнг батлахгүйг харууллаа. Тусгай B8 тест `present=5`, хамгийн урт цуваа 3 гэсэн өгөгдлөөр алдааг илрүүлсэн. `total` мутаци тестэд нөлөөлөөгүй тул PMD-ийн unused-local дүрэм хэрэг болов.
