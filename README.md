## How ODATA operations work

The ODATA operations are similar with REST operations, getting an 
input data and returning a result. The developer has the access to
the http status code as well. 

The main artifacts of this application:

- ODataController class - is a regular springboot controller, the
entry point to the OData implementation. The class defines a number of artifacts
needed to expose the OData / OLingo functionality. This class
should not change unless other type of processors need to 
be added 
- DemoEDMProvider - this is the place where metadata is defined. Any change
to the data structure or the action array exposed is reflected in this class
DemoActionComplexProcessor - olingo class where the actual action 
invocations are processed. All the actions that take as a parameter
a complex value and return a complex value are redirected to be processed here
- ODataConst - constants class. While not mandatory, it is a good practice
to keep the OData action and namespaces definition in one place
- Utility classes that convert a complex type to a Java value object and 
viceversa. 
- Processor classes for each action, where the actual processing activity is being delegated

Current implementation: a test action is provided. ODAta const defines
the name and the namespaces associated with the action

```java
    // Action
    public static final String ACTION_GET_BIG_VALUE = "GetBigValue";
    public static final FullQualifiedName ACTION_GET_BIG_VALUE_FQN = new FullQualifiedName(NAMESPACE, ACTION_GET_BIG_VALUE);
```

Types associated with this action are also defined in the ODataConst. 

DemoEdmType defines all the complex types and actions associated with the implementation, as follows:

getComplexType - based on full qualified name of the complex type returns a full description of that type. Please 
note that the property names should be stored in a constant file, as they are used in multiple places
and any other approach could lead to inconsistencies. 

getEntityContainer and getEntityContainerInfo - methods define the entity container. These never change and the default
implementation provided is the only implementation needed. 

getAction and getActionImport - those change as soon as a new action is being added. The name and
the FQN are already defined in ODataConst (if not, start with that) and then replicate the code
that is provided for the test action. 

The implementation: this exercise is designed only for actions (functions) 
that take a parameter a complex value and return another complex value. The implementation
is done in the DemoActionComplexProcessor and delegated to one implementation
of IComplexValueProcessor for each action. This design with IComplexValueProcessor
is not part of the Olingo or OData but part of the application design
in order to keep the application modular when it becomes big (a lot of
actions). Some design is needed as well to delegate the implementation
of the DemoEdmProvider class when the number of complex types increases
beyond a limit. 
