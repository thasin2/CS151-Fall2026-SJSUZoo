# CS151-Fall2026-SJSUZoo

## Overview

SJSU Zoo is a text-based Java program that simulates the daily operation of a zoo.
Users manage animals, habitats, staff, visitors, attractions, and animal food through a console menu.
The system models real zoo work such as feeding animals, inspecting and cleaning habitats, running staff payroll, and admitting visitors.

This project is for CS 151 (Object-Oriented Design), Fall 2026, at San Jose State University.

## Developers

| Developer | Classes owned | Area of responsibility |
| --- | --- | --- |
| Thaneesha Singh | `Animal`, `Habitat` | Animal care data, habitat capacity and environment rules |
| Nam Vo | `Food` (Diet), `Attraction` | Animal food stock and shipments, attractions and pricing |
| Bao Tran | `Staff` (abstract), `Zookeeper`, `Veterinarian` | Staff roles, shifts, pay, and animal care permissions |
| Angelica Perez | `Zoo`, `Visitor` | Central zoo management, visitor admission and membership |

## Design

Planned structure:

| Type | Name | Purpose |
| --- | --- | --- |
| Abstract class | `Staff` | Shared staff behavior. Each role defines its own pay and care rules. |
| Subclass | `Zookeeper` | Feeds animals and cleans and inspects assigned habitats. |
| Subclass | `Veterinarian` | Examines, quarantines, and releases animals. |
| Interface | `Maintainable` / `Inspectable` (name to be finalized) | Shared inspection and upkeep behavior. |
| Class | `Animal` | An animal in the zoo. |
| Class | `Habitat` | An enclosure that holds animals. |
| Class | `Food` | Animal food stock. |
| Class | `Attraction` | A visitor attraction with a price and a daily capacity. |
| Class | `Visitor` | A zoo guest with a ticket and an optional membership. |
| Class | `Zoo` | Holds and manages all of the objects above. |

The UML class diagram will be added at the root of this repository.

## Installation Instructions

To be added when the build setup is finalized.

## Usage

To be added when the menu is implemented.

## Contributions

| Developer | Contributions |
| --- | --- |
| Thaneesha Singh | To be updated |
| Nam Vo | To be updated |
| Bao Tran | README setup with team ownership |
| Angelica Perez | To be updated |
