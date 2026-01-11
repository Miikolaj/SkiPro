# Snippety do dokumentacji (do wklejenia do SkiPro.pdf)

## Atrybut asocjacji 1-*
W systemie SkiPro atrybuty asocjacji dla relacji 1-* są realizowane jako osobne encje ("association class"), np. `Employment` dla relacji `Resort (1) → Employment (*)` oraz `Instructor (1) → Employment (*)`. Encja `Employment` przechowuje dane należące do samego powiązania (np. `startDate`, `endDate`), a nie do pojedynczej klasy.

## ORM + owning side / mappedBy (bez ręcznego SQL)
Relacje między encjami są odwzorowane obiektowo w JPA/Hibernate jako kolekcje (`Set<>`) z określoną krotnością, bez ręcznego pisania zapytań SQL. Dla relacji dwustronnych rozróżniam stronę posiadającą relację (**owning side**, np. `Employment.resort`/`Employment.instructor` z `@JoinColumn`) oraz stronę odwrotną (**inverse side**, np. `Resort.employments` i `Instructor.employments` z `mappedBy`). Modyfikacja asocjacji odbywa się przez metody domenowe aktualizujące powiązane kolekcje/klucze obce w ramach transakcji, a ORM automatycznie synchronizuje strukturę bazy danych (w tym tabele pośrednie).

