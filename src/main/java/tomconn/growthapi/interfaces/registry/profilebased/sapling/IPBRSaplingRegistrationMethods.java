package tomconn.growthapi.interfaces.registry.profilebased.sapling;

import net.minecraft.block.Block;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import tomconn.growthapi.interfaces.growthprofile.IGrowthProfile;
import tomconn.growthapi.interfaces.registry.profilebased.IProfileBasedRegistry;

/**
 * IPBR is the abbreviation for {@link IProfileBasedRegistry}
 */
public interface IPBRSaplingRegistrationMethods {

    /**
     * Registers a profile in this registry instance.
     *
     * @param blockClass the class of the sapling
     * @param profile its respective growth profile
     * @return true if and only if this sapling was registered and no secondary side-effects were encountered
     */
    boolean registerSaplingProfile(Class<? extends Block> blockClass, IGrowthProfile<SaplingGrowTreeEvent> profile);

    /**
     * A {@link Tuple}-array based wrapper for {@link #registerSaplingProfile(Class, IGrowthProfile)} (Class, IGrowthProfile)}
     * @see #registerSaplingProfile(Class, IGrowthProfile)
     *
     * @return an array of booleans which are index-associated with the respective inputs in the parameter
     */
    boolean[] registerSaplingProfiles(
            Tuple<
                    Class<? extends Block>,
                    IGrowthProfile<SaplingGrowTreeEvent>
                    >... profiles
    );
}
