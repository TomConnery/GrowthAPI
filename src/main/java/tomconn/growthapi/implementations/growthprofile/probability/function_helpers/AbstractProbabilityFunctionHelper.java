package tomconn.growthapi.implementations.growthprofile.probability.function_helpers;

import net.minecraftforge.fml.common.eventhandler.Event;
import tomconn.growthapi.interfaces.event.helper.BaseEventHelper;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.MathematicalFunction;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.Probability;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.ProbabilityFunction;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.ProbabilityFunctionTuple;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.container.CoDomainContainer;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.container.DomainContainer;
import tomconn.growthapi.interfaces.growthprofile.probability.math.function.container.interval.Interval;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to ease the creation of abstract {@link ProbabilityFunction}s
 *
 * @param <E> any inheritor of {@link Event}
 *
 * @since 0.0.6
 */
public abstract class AbstractProbabilityFunctionHelper< E extends Event > {

    /**
     * Tailors a {@link ProbabilityFunction} based on the passed {@link ProbabilityFunctionTuple}s and supplier for the
     * value
     *
     * @param tuples        the tuples
     * @param valueSupplier the supplier for the value
     * @param <T>           the type of the domain
     * @param <D>           a {@link DomainContainer}
     *
     * @return a {@link ProbabilityFunction} which is based on the passed arguments
     *
     * @since 0.0.6
     */
    @Nonnull
    protected < T, D extends DomainContainer< T > > ProbabilityFunction< E > tailorFunction(@Nonnull Collection< ? extends ProbabilityFunctionTuple< T, D, ? > > tuples, @Nonnull Function< E, T > valueSupplier) {

        Objects.requireNonNull(tuples);
        Objects.requireNonNull(valueSupplier);

        return e -> {
            T value = valueSupplier.apply(e);

            return () -> fetchProbability(tuples, value);
        };
    }


    /**
     * Fetches the chance which includes the <code>value</code> from a passed {@link Collection}
     *
     * @param tuples a collection of {@link ProbabilityFunctionTuple}s, having {@link Interval}s set as their type
     * @param value  the value
     * @param <T>    the type of the <code>intervals</code>
     * @param <C>    a {@link DomainContainer}
     *
     * @return a single {@link Interval}
     *
     * @throws MathematicalFunction.MissingMappingException if there was no mathing {@link Interval} present within the
     *                                                      {@link Collection}
     * @throws MathematicalFunction.MultiMappingException   if there were multiple matching {@link Interval}s present
     *                                                      within the {@link Collection}
     * @see #checkMappings(Collection, Object)
     * @since 0.0.6
     */
    private < T, C extends DomainContainer< T > > Probability fetchProbability(@Nonnull Collection< ? extends ProbabilityFunctionTuple< T, C, ? > > tuples, @Nonnull T value) {

        Objects.requireNonNull(tuples);
        Objects.requireNonNull(value);

        Stream< CoDomainContainer< Probability > > stream = filteredStream(tuples, value);

        Set< Probability > set = stream.map(CoDomainContainer::getValue).collect(Collectors.toSet());

        checkMappings(set, value);

        return set.iterator().next();


    }


    /**
     * Derives a {@link Stream} from the passed {@link Collection} which has an included filter that selects all
     * eligible probabilities, based on the {@link ProbabilityFunctionTuple}s within the {@link Collection}
     *
     * @param tuples a {@link Collection} of {@link ProbabilityFunctionTuple}s with type-containers
     * @param value  a value
     * @param <T>    the type of the value
     * @param <C>    a {@link DomainContainer}
     *
     * @return a filtered {@link Stream}
     *
     * @since 0.0.6
     */
    private < T, C extends DomainContainer< T > > Stream< CoDomainContainer< Probability > > filteredStream(@Nonnull Collection< ? extends ProbabilityFunctionTuple< T, C, ? > > tuples, T value) {

        Objects.requireNonNull(tuples);
        Objects.requireNonNull(value);

        return tuples.stream()
                .filter(t -> t.getDomainContainer().isValuePresent(value))
                .map(ProbabilityFunctionTuple::getProbabilityContainer);
    }


    /**
     * This method checks whether the passed mappings are following the definition of {@link ProbabilityFunction}s.
     *
     * @param probabilities the mappings in the form of a {@link Collection} of {@link Probability}s as chances
     * @param value         the value which has these mappings within the {@link ProbabilityFunction}
     * @param <T>           the type of the domain
     *
     * @throws MathematicalFunction.MissingMappingException thrown if the {@link Collection} is empty
     * @throws MathematicalFunction.MultiMappingException   thrown if there is more than one mapping within the {@link
     *                                                      Collection}
     * @since 0.0.6
     */
    private < T > void checkMappings(@Nonnull Collection< Probability > probabilities, @Nonnull T value) throws MathematicalFunction.MissingMappingException, MathematicalFunction.MultiMappingException {

        Objects.requireNonNull(probabilities);
        Objects.requireNonNull(value);

        // a function is bound to only have one associated element per domain-element
        if (probabilities.size() > 1) {
            throw new MathematicalFunction.MultiMappingException(
                    value.toString(),
                    probabilities.stream()
                            .map(Object::toString)
                            .collect(Collectors.toList())
            );
        }

        // a function is bound to assign a value to *every* element of the domain
        if (probabilities.size() == 0) {
            throw new MathematicalFunction.MissingMappingException(value.toString());
        }

    }


    /**
     * Returns a {@link BaseEventHelper} which is specialized for the event-type
     *
     * @param event an instance of the event
     *
     * @return a {@link BaseEventHelper}
     *
     * @since 0.0.6
     */
    @Nonnull
    protected abstract BaseEventHelper supplyHelper(@Nonnull E event);


}
