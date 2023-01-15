# Inter-Scene Communication

## `Scene`s should communicate in two ways.

- Through `private static` fields that can be set with a `static` setter, which may take in as a parameter, the class of the scene *giving* the data (to avoid getting data from an untrusted source!).

- Through "Communicator" classes, which keep fields with data, `private`.<br>
They also have `public` getter methods for the data.
These classes may be singletons.

The example code in the `static_fields` and `comm_class` sub-packages of the `nerd_test.scene_comms` package should clarify these ideas' implementations.
