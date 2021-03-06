package tomconn.growthapi.implementations.growthprofile;


import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import tomconn.growthapi.implementations.requirementhelpers.RequirementHelpers;

import javax.annotation.Nonnull;

/**
 * A {@link SaplingGrowTreeEvent} based implementation of {@link AbstractBaseGrowthProfile}
 *
 * @since 0.0.5
 */
class SaplingGrowthProfile extends AbstractBaseGrowthProfile< SaplingGrowTreeEvent, SaplingGrowthProfile > {

    /**
     * Default constructor
     *
     * @since 0.0.5
     */
    SaplingGrowthProfile() {

        super(RequirementHelpers.sapling());
    }


    /**
     * {@inheritDoc}
     *
     * @since 0.0.6
     */
    @Nonnull
    @Override
    public SaplingGrowthProfile getThis() {

        return this;
    }

}
