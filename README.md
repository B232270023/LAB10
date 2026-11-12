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
