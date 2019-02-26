package com.androiddesdecero.streamandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    /*
    Un Stream representa una secuencia de elementos, por ejemplo una Lista en la que se admiten
    diferentes tipos de operaciones para realizar cáculos sobre esos elementos.
    Un Stream es un conjunto de funciones que se ejecutan de forma anidada.
    Tenemos diferentes operaciones que se pueden anidar en un Stream.
    Las más conocidas nos permtien Reducir, Recopilar, y Map.

    Los Stream son envoltorios alredor de una fuente de datos, que nos permite operar con esa
    fuente de datos y hace que el procesamiento masivo sea conveniente y rápido.
    Un Stream no almacena datos. No es una estructura de datos.
     */

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUser();

        /*
        Crear un Stream
         */
        //Crear un Stream
        Stream.of(users);
        //or Crear un Stream desde una lista que ya existe.
        users.stream();

        /*
        Operaciones Stream
         */

        //ForEach
        //La más simple y común en los Stream. Recorre los elementos del flujo de datos, llamadno a la función suministrada
        //para cada elemento.
        users.stream().forEach(e -> e.setNombre(e.getNombre() + " Apellido"));
        imprimirLista();
        //Foreach es una operación terminal, es decir una vez realizada la operación, se considera el stream consumido.

        //map
        //Map nos permite realizar una transformación rápida, en una sola línea de código
        //de los datos del flujo original del Stream.
        //En este caso el stream original es una lista de User y nos va a devolver una Lista de String.
        List<String> lista = users.stream().map(x->x.getNombre()).collect(Collectors.toList());
        lista.stream().forEach(e -> Log.d("TAG1", e + ""));

        //Collect
        //Nos permite sacar cosas de un Stream una vez hayamos finalizado el proceso.
        //En el caso de más arriba nos permite convertir el Stream a una lista Stream
        //utiliando la clase Collectos y su método toList();

        //Filter
        //Produce una nueva secuencia que cotienen elementos del stream original que han pasado por una
        //determinada prueba especificada por un Predicado
        Log.d("TAG1", "------------------------Filters-------------------------");
        setUpUser();
        List<User> usersFilters =  users.stream()
                .filter(e -> e.getNombre() != "Alberto")
                .filter(e -> e.getId() <3)
                .collect(Collectors.toList());
        usersFilters.stream().forEach(e -> Log.d("TAG1", e.getNombre()));

        //FindFirst
        //Devuelve un optional para el primer elemento del Stream. Al ser un opciónal puede ser empty.
        Log.d("TAG1", "---------------------Find First----------------------------");
        setUpUser();
        User user =  users.stream()
                .filter(e -> e.getId() < 4)
                .findFirst()
                .orElse(null);
        Log.d("TAG1", user.getNombre());

        //flatMap
        //Obtiene los datos de diferentes arrays y los concatena en un único stream
        Log.d("TAG1", "---------------------FlatMap----------------------------");
        List<List<String>> nombresVariasListas = new ArrayList<List<String>>(
                Arrays.asList(
                        new ArrayList<String>(Arrays.asList("Alberto", "Maria", "Pedro")),
                        new ArrayList<String>(Arrays.asList("Mónica", "Pablo"))
                )
        );

        List<String> nombresUnaLista = nombresVariasListas
                .stream()
                .flatMap(e -> e.stream())
                .collect(Collectors.toList());
        nombresUnaLista.stream().forEach(e -> Log.d("TAG1", e));

        //Peek -> Similar a forEach, pero sin ser una acción final, sino que se puede seguir haciendo
        // cosas en el stream
        //ForEarch ->La más simple y común en los Stream. Recorre los elementos del flujo de datos, llamadno a la función suministrada
        //para cada elemento.
        Log.d("TAG1", "---------------------Peek----------------------------");
        setUpUser();
        users.stream().peek(e -> e.setNombre(e.getNombre() + " Apellido"))
                .collect(Collectors.toList());
        imprimirLista();

        /*
        Recapitulando sobre los Stream
        Vemos que primero tenemos una fuente de flujo "Source Stream" seguido de 0 o más
        Operaciones Intermedias y una operación terminal.
         */
        Log.d("TAG1", "---------------------Count----------------------------");
        setUpUser();
        long numeroFiltrado = users.stream()
                .filter(e -> e.getId() <3)
                .count();
        Log.d("TAG1", numeroFiltrado + "");
        Log.d("TAG1", "---------------------Skip y Limit----------------------------");
        String[] abc = {"a", "b", "c", "d", "e","f", "g", "h", "i", "j"};
        List<String> abcFilter = Arrays.stream(abc)
                .skip(2)
                .limit(4)
                .collect(Collectors.toList());;
        abcFilter.stream().forEach(e->Log.d("TAG1", e));

        Log.d("TAG1", "---------------------Sorted----------------------------");
        setUpUser();
        users = users.stream()
                .sorted(Comparator.comparing(User::getNombre))
                .collect(Collectors.toList());
        imprimirLista();

        Log.d("TAG1", "---------------------Min and Maz----------------------------");
        setUpUser();
        User userMin = users.stream()
                .min((e1, e2) -> e1.getId() - e2.getId())
                .orElse(null);
        Log.d("TAG1", userMin.getId() + "");
        User userMax = users.stream()
                .max(Comparator.comparing(User::getId))
                .orElse(null);
        Log.d("TAG1", userMax.getId() + "");

        /*
        Elimina los elementos duplicados
         */
        Log.d("TAG1", "---------------------distinct----------------------------");
        String[] abc1 = {"a", "b", "c", "d", "e","f", "g", "g", "i", "j","a"};
        List<String> abcFilter1 = Arrays.stream(abc1)
                .distinct().collect(Collectors.toList());
        abcFilter1.stream().forEach(e->Log.d("TAG1", e));

        /*
        allMatch -> Verifica si el predicado es verdadero. Si lo es devuelve true, sino false.
        anyMatch -> Verifica si al menos 1 valor del predicado es Verdadero.
        nonematch -> Verifica si ningun elemento pasa el predicado.
         */
        Log.d("TAG1", "---------------------allMatch----------------------------");
        List<Integer> listaNumeros = Arrays.asList(1000, 300, 900, 5000);

        boolean allMatch = listaNumeros.stream().allMatch(e -> e >301);
        Log.d("TAG1", "Son todos mayor que: " + allMatch);

        boolean anyMatch = listaNumeros.stream().anyMatch(e -> e >301);
        Log.d("TAG1", "Al menos un valor es mayor que: " + anyMatch);

        boolean noneMatch = listaNumeros.stream().noneMatch(e -> e >10000);
        Log.d("TAG1", "Ningun valor es mayor que : " + anyMatch);

        Log.d("TAG1", "---------------------Sum Avarege  range----------------------------");
        setUpUser();
        double result = users.stream()
                .mapToInt(User::getId)
                .average()
                .orElse(0);
        Log.d("TAG1", "Average : " + result);
        result = users.stream()
                .mapToInt(User::getId)
                .sum();
        Log.d("TAG1", "suma : " + result);
        Log.d("TAG1", IntStream.range(0, 100).sum()+"");


        /*
        Una operación de reducción. Tambien llamada fold toma el Stream y los convina en un
        único resultado mediante la aplicación repetida de una operación.
        En este ejemplo sumamos el valor de todos los ids
        */
        Log.d("TAG1", "---------------------reduce----------------------------");
        setUpUser();
        int numero = users.stream()
                .map(User::getId)
                .reduce(0, Integer::sum);
        Log.d("TAG1", "reduce : " + numero);

        /*
        El método joing devuelve un recopilador ue concatena la secuencia de CharSequence y devuelve el resultado
        como una cadena.
         */
        Log.d("TAG1", "---------------------joining----------------------------");
        setUpUser();
        String names = users.stream()
                .map(User::getNombre)
                .collect(Collectors.joining(" - "))
                .toString();
        Log.d("TAG1", names);

        /*
        Collectos.toSet devuelve un collector que acumula los elementos de entrada en
        nuevo SET.
        Como ya sabemos SET no nos garantiza que el orden de recolección de los elementos
        sea el mismo que el de entrada, es decir que será desordenado.
        Pero además nos garantiza que no habrá elementos repetidos.
         */
        Log.d("TAG1", "---------------------set----------------------------");
        setUpUser();
        Set<String> setNames = users.stream()
                .map(User::getNombre)
                .collect(Collectors.toSet());
        setNames.stream().forEach(e->Log.d("TAG1", e));

        /*
        summarizingDouble -> Nos devuelve estadisticas interesantes. En una variable
        DoubleSummaryStatis
         */
        Log.d("TAG1", "---------------------sumnarizingDouble()----------------------------");
        setUpUser();
        DoubleSummaryStatistics statistics = users.stream()
                .collect(Collectors.summarizingDouble(User::getId));
        Log.d("TAG1", statistics.getAverage() + " " +  statistics.getMax() + " "
                + statistics.getSum() + " " + statistics.getCount() + " " + statistics.getSum());
        //Otra forma de Hacerlo
        DoubleSummaryStatistics statistics1 = users.stream()
                .mapToDouble(User::getId)
                .summaryStatistics();
        Log.d("TAG1", statistics1.getAverage() + " " +  statistics1.getMax() + " " + statistics1.getSum());
        /*
        PartitioningBy -> Partir en 2 un stream. Es decir generamos 2 stream
         */
        Log.d("TAG1", "---------------------PartitioningBy----------------------------");
        List<Integer> numeros = Arrays.asList(5,7,34,56,2,3,67,4,98);
        Map<Boolean, List<Integer>> esMayor = numeros.stream()
                .collect(Collectors.partitioningBy(e->e>10));
        esMayor.get(true).stream().forEach(e->Log.d("TAG1", e + ""));
        esMayor.get(false).stream().forEach(e->Log.d("TAG1", e + ""));
        /*
        groupingBy-> Agrupamos según queramos. Es decir que se debe indicar
        la propiedad por la cual se realizará la agrupación.
         */
        Log.d("TAG1", "---------------------groupingBy----------------------------");
        setUpUser();
        Map<Character, List<User>> grupoAlfabetico = users.stream()
                .collect(Collectors.groupingBy(e -> new Character(e.getNombre().charAt(0))));

        grupoAlfabetico.get('A').stream().forEach(e->Log.d("TAG1", e.getNombre()));
        grupoAlfabetico.get('M').stream().forEach(e->Log.d("TAG1", e.getNombre()));

        /*
        Mapping-> Convierte una lista de objetos en otra lista de objetos que queramos
         */
        Log.d("TAG1", "---------------------Mapping----------------------------");
        setUpUser();
        List<String> personas = users.stream()
                .collect(Collectors.mapping(User::getNombre, Collectors.toList()));
        personas.stream().forEach(e->Log.d("TAG1", e));

        /*
        Los Stream son envoltorios alredor de una fuente de datos, que nos permite operar con esa
        fuente de datos y hace que el procesamiento masivo sea conveniente y rápido.
        Hemos visto una serie de ejemplos en los que nos indican el potencial de usar
        stream en lugar dela forma de programación que veniamos haciendo.
        Una consejo, cuando tengas que realizar una operación con una lista, piensa
        que casi seguro hay una operación de stream que la puede hacer. Así que el objetivo
        es buscarla y luego aplicarla.
        Y escribir nuestro código de forma elegante.
         */



    }

    private void setUpUser(){
        users = new ArrayList<>();
        users.add(new User(1,"Alberto"));
        users.add(new User(2,"Marta"));
        users.add(new User(3,"Maria"));
        users.add(new User(4,"Pablo"));
        users.add(new User(5,"Adolfo"));
        users.add(new User(1,"Alberto"));

    }

    private void imprimirLista(){
        users.stream().forEach(e -> Log.d("TAG1", e.getNombre() + ""));
    }
}