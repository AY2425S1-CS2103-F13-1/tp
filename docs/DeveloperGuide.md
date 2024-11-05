---
layout: page
title: Developer Guide
---

- Table of Contents
{:toc}

---

## **Acknowledgements**

- {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

---

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

### Architecture

{% include DeveloperGuide/Design/architecture.md %}

### UI component

{% include DeveloperGuide/Design/ui.md %}

### Logic component

{% include DeveloperGuide/Design/logic.md %}

### Model component

{% include DeveloperGuide/Design/model.md %}

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550"  alt="Storage Class Diagram"/>

The `Storage` component,

* can save address book data, transaction book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from `AddressBookStorage`, `TransactionBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)


### Common classes

Classes used by multiple components are in the `spleetwaise.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

- `VersionedAddressBook#commit()` — Saves the current address book state in its history.
- `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
- `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

- **Alternative 1 (current choice):** Saves the entire address book.
  - Pros: Easy to implement.
  - Cons: May have performance issues in terms of memory usage.

- **Alternative 2:** Individual command knows how to undo/redo by itself.
  - Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  - Cons: We must ensure that the implementation of each individual command are correct.

**Aspect: spleetwaise.commons Package:**

- **Disclaimer:** Our team has decided to add the `spleetwaise.commons` package as a common package for classes that are used by multiple components, in our case, `address` and `transaction`. This is an enhancement for modularity on top of the original design of the AddressBook-Level3 project, which only have a `seedu.address` package. The refactoring is almost 90% complete, and we are working on the remaining 10%.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

{% include DeveloperGuide/Requirements/index.md %}

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">
:information_source: **Note:** These instructions only provide a starting point for testers to work on; testers are expected to do more *exploratory* testing.
</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder
   
   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   
   2. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Local GUI testing

The available gradle tasks are: guiTests, nonGuiTests, allTests.

- guiTests: all tests under `systemtests` package
- nonGuiTests: all tests under `spleetwaise.address`, `spleetwaise.common`, `spleetwaise.transaction` packages
- allTests: guiTests and nonGuiTests, nonGuiTests will be run before guiTests

As an example, you can run `gradle nonGuiTests` in the gradle terminal for all tests excluding GUI related tests.
You can navigate the gradle terminal by clicking on elephant icon _(Gradle)_ > terminal icon _(Execute Gradle tasks)_.

### Adding a person

1. Adding a person while all persons are being shown

   1. Prerequisites: List all persons using the list command. Multiple persons in the list.
   
   2. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01`<br>
      Expected: A new contact named John Doe is added to the list. Details of the added contact shown in the status message.
   
   3. Test case: `add n/ p/ e/ a/`<br>
      Expected: No person is added. Error details shown in the status message. Status bar remains the same.
   
   4. Other incorrect add commands to try: `add n/John Doe`, `add p/98765432`, `add e/johnd@example.com`, `add a/John street, block 123, #01-01`<br>
      Expected: Similar to previous.

### Editing a person

1. Editing a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `edit 1 n/Jane Doe`<br>
      Expected: First contact's name is changed to Jane Doe. Details of the edited contact shown in the status message.

   3. Test case: `edit 0 n/Jane Doe`<br>
      Expected: No person is edited. Error details shown in the status message.

   4. Other incorrect edit commands to try: `edit`, `edit x n/Jane Doe` (where x is larger than the list size),
      `edit 1`<br>
      Expected: Similar to previous.

### Person Synchronization in transaction list

1. Ensuring person details changed are accurately reflected in all views and models.

   1. Prerequisites: List all persons and transactions using the `list` and `listTxn` command. At least one person and one transaction with that person in the list.
   
   2. Test case: `edit 1 n/New Name` (where `New Name` is the updated name for the person)<br>
      Expected: The person's name updates in the lists and UI, ensuring consistency in both `CommonModel` and the visible transaction list.

### Adding a remark for a person

1. Adding a remark for a person using the `remark` command while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `remark 1 r/Likes to swim`<br>
      Expected: First contact's remark is updated to "Likes to swim". Details of the updated contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `remark 0 r/Likes to swim`<br>
      Expected: No person's remark is updated. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect remark commands to try: `remark`, `remark x r/Likes to swim` (where x is larger than the list size), `remark 1`<br>
     Expected: Similar to previous.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. _{ more test cases …​ }_

### Marking a transaction as done

1. Marking a transaction as done while all transactions are being shown. 

   1. Prerequisites: List all transactions using the `listTxn` command. One transaction is in the list.

   2. Test cases: `markDone 1`<br>
      Expected: The first transaction is marked as done. A "done" icon appears next to the person's name for that transaction. Details of the updated transaction shown in the status message.

   3. Test cases: `markDone 1` (Assumes transaction 1 is already marked)<br>
      Expected: No change in transaction status. The "done" icon remains. A status message confirms that the transaction is already marked.
   
   4. Test cases: `markDone 0`<br>
      Expected: No transaction is marked. Error details shown in the status message.
   
   5. Other incorrect `markDone` commands to try: `markDone`, `markDone x` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Reverting a done transaction back to undone

1. Reverting a done transaction back to undone while all transactions are being shown.

    1. Prerequisites: List all transactions using the `listTxn` command. One transaction is in the list.

    2. Test cases: `markUndone 1`<br>
       Expected: The first transaction is reverted to undone. The existing "done" icon disappears for that transaction. Details of the updated transaction shown in the status message.

    3. Test cases: `markUndone 1` (Assumes transaction 1 is already undone)<br>
       Expected: No change in transaction status. The transaction remains to have no "done" icon. A status message confirms that the transaction is already undone.

    4. Test cases: `markUndone 0`<br>
       Expected: No transaction is marked. Error details shown in the status message.

    5. Other incorrect `markUndone` commands to try: `markUndone`, `markUndone x` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Filter Reuse in Transaction List

1. Maintaining the current filter state when transactions are modified.

    1. Prerequisites: List all transactions using the `listTxn` command. Apply a filter via `filterTxn` command to the list (e.g., filtering by description containing "mac").  

    2. Test cases: `addTxn p/01234567 d/macdonald`<br>
       Expected: The new transaction appears in the filtered list while preserving the existing filter. Details of the new transaction shown in the status message.
   
    3. Test cases: `editTxn 1 d/happy meal at mac` (Assumes transaction 1 description is "KFC")<br>
       Expected: The updated transaction appears in the filtered list while preserving the existing filter. Details of the updated transaction shown in the status message.

    4. Test cases: `editTxn 1 d/KFC` (Assumes transaction 1 description is "fries at mac")<br>
       Expected: The updated transaction disappears in the filtered list while preserving the existing filter. Details of the updated transaction shown in the status message.

    5. Test cases: `markDone 1`, `markUndone 1` (Assumes transaction 1 description is "fries at mac")<br>
       Expected: The transaction done icon updated in the filtered list while preserving the existing filter. Details of the updated transaction shown in the status message.

### Default Behavior on App Startup

1. Verifying filter state upon app initialization.

    1. Prerequisites: At least one done transaction and one undone transaction in the list.

    2. Test cases: Initial Filter on App Startup<br> 
       Expected: The list displays all transactions (both done and undone) by default when the app starts.

### Saving data

1. Dealing with missing/corrupted data files

   1. Prerequisites: Multiple persons in `addressbook.json` and multiple transactions in `transactionbook.json` data files.<br>
       _{explain how to simulate a missing/corrupted file, and the expected behavior}_
   
   2. Test cases: Missing `isDone` field in `transactionbook.json` data file<br>
      Simulation: Remove the `isDone` field from a transaction entry in the JSON file, then start the app.
      Expected: The transaction loads as undone by default. Upon closing the app, the transaction is saved as undone in the JSON file.
   
   3. 

2. _{ more test cases …​ }_
