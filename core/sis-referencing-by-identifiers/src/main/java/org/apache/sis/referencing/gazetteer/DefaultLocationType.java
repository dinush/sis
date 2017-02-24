/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.referencing.gazetteer;

import java.util.Map;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import org.opengis.util.InternationalString;
import org.opengis.metadata.citation.Party;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.referencing.gazetteer.LocationType;
import org.opengis.referencing.gazetteer.ReferenceSystemUsingIdentifiers;
import org.apache.sis.internal.util.UnmodifiableArrayList;
import org.apache.sis.metadata.ModifiableMetadata;
import org.apache.sis.util.ArgumentChecks;


/**
 * Unmodifiable description of a location.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.8
 * @version 0.8
 * @module
 */
final class DefaultLocationType implements LocationType {
    /**
     * Name of the location type.
     */
    private final InternationalString name;

    /**
     * Property used as the defining characteristic of the location type.
     */
    private final InternationalString theme;

    /**
     * Method(s) of uniquely identifying location instances.
     */
    private final List<InternationalString> identifications;

    /**
     * The way in which location instances are defined.
     */
    private final InternationalString definition;

    /**
     * The reference system that comprises this location type.
     */
    private final ReferenceSystemUsingIdentifiers referenceSystem;

    /**
     * Geographic area within which the location type occurs.
     */
    private final GeographicExtent territoryOfUse;

    /**
     * Name of organization or class of organization able to create and destroy location instances.
     */
    private final Party owner;

    /**
     * Parent location types (location types of which this location type is a sub-division).
     */
    private final List<LocationType> parents;

    /**
     * Child location types (location types which sub-divides this location type).
     */
    private final List<LocationType> children;

    /**
     * Creates a copy of the given location type with the reference system set to the given value.
     *
     * @param source    the location type to use as a template.
     * @param rs        the reference system that comprises this location type.
     * @param existing  other {@code DefaultLocationType} instances created before this one.
     */
    @SuppressWarnings("ThisEscapedInObjectConstruction")
    private DefaultLocationType(final LocationType source, final ReferenceSystemUsingIdentifiers rs,
            final Map<LocationType, DefaultLocationType> existing)
    {
        /*
         * Put 'this' in the map at the beginning in case the parents and children contain cyclic references.
         * Cyclic references are not allowed if the source are LocationTypeTemplate, but the user could have
         * given its own implementation. Having the 'this' reference escaped in object construction should not
         * be an issue here because this is a private constructor, and we use it in such a way that if an
         * exception is thrown, the whole tree (with all 'this' references) will be discarded.
         */
        existing.put(source, this);
        name            = source.getName();
        theme           = source.getTheme();
        identifications = snapshot(source.getIdentifications());
        definition      = source.getDefinition();
        territoryOfUse  = (GeographicExtent) unmodifiable(source.getTerritoryOfUse());
        owner           = (Party) unmodifiable(source.getOwner());
        parents         = snapshot(source.getParents(),  rs, existing);
        children        = snapshot(source.getChildren(), rs, existing);
        referenceSystem = rs;
    }

    /**
     * Copies the content of the given collection.
     *
     * @param rs        the reference system to assign to the new location types.
     * @param existing  an initially empty identity hash map for internal usage by this method.
     */
    static List<LocationType> snapshot(final Collection<? extends LocationType> types,
            final ReferenceSystemUsingIdentifiers rs, final Map<LocationType, DefaultLocationType> existing)
    {
        final LocationType[] array = types.toArray(new LocationType[types.size()]);
        for (int i=0; i < array.length; i++) {
            final LocationType source = array[i];
            ArgumentChecks.ensureNonNullElement("types", i, source);
            DefaultLocationType copy = existing.get(source);
            if (copy == null) {
                copy = new DefaultLocationType(source, rs, existing);
            }
            array[i] = copy;
        }
        switch (array.length) {
            case 0:  return Collections.emptyList();
            case 1:  return Collections.singletonList(array[0]);
            default: return UnmodifiableArrayList.wrap(array);
        }
    }

    /**
     * Returns the given collection as an unmodifiable list.
     */
    @SuppressWarnings("unchecked")
    private static List<InternationalString> snapshot(final Collection<? extends InternationalString> c) {
        if (c instanceof UnmodifiableArrayList<?>) {
            return (List<InternationalString>) c;
        } else {
            return UnmodifiableArrayList.wrap(c.toArray(new InternationalString[c.size()]));
        }
    }

    /**
     * Returns an unmodifiable copy of the given metadata, if necessary and possible.
     */
    private static Object unmodifiable(final Object metadata) {
        if (metadata instanceof ModifiableMetadata) {
            return ((ModifiableMetadata) metadata).unmodifiable();
        } else {
            return metadata;
        }
    }

    /**
     * Returns the name of the location type.
     *
     * <div class="note"><b>Examples:</b>
     * “administrative area”, “town”, “locality”, “street”, “property”.</div>
     *
     * @return name of the location type.
     */
    @Override
    public InternationalString getName() {
        return name;
    }

    /**
     * Returns the property used as the defining characteristic of the location type.
     *
     * <div class="note"><b>Examples:</b>
     * <cite>“local administration”</cite> for administrative areas,
     * <cite>“built environment”</cite> for towns or properties,
     * <cite>“access”</cite> for streets,
     * <cite>“electoral”</cite>,
     * <cite>“postal”</cite>.</div>
     *
     * @return property used as the defining characteristic of the location type.
     *
     * @see ReferenceSystemUsingIdentifiers#getTheme()
     */
    @Override
    public InternationalString getTheme() {
        return theme;
    }

    /**
     * Returns the method(s) of uniquely identifying location instances.
     *
     * <div class="note"><b>Examples:</b>
     * “name”, “code”, “unique street reference number”, “geographic address”.</div>
     *
     * @return method(s) of uniquely identifying location instances.
     */
    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")         // Because unmodifiable
    public Collection<InternationalString> getIdentifications() {
        return identifications;
    }

    /**
     * Returns the way in which location instances are defined.
     *
     * @return the way in which location instances are defined.
     */
    @Override
    public InternationalString getDefinition() {
        return definition;
    }

    /**
     * Returns the geographic area within which the location type occurs.
     *
     * <div class="note"><b>Examples:</b>
     * the geographic domain for a location type “rivers” might be “North America”.</div>
     *
     * @return geographic area within which the location type occurs.
     */
    @Override
    public GeographicExtent getTerritoryOfUse() {
        return territoryOfUse;
    }

    /**
     * Returns the reference system that comprises this location type.
     *
     * @return the reference system that comprises this location type.
     */
    @Override
    public ReferenceSystemUsingIdentifiers getReferenceSystem() {
        return referenceSystem;
    }

    /**
     * Returns the name of organization or class of organization able to create and destroy location instances.
     *
     * @return organization or class of organization able to create and destroy location instances.
     */
    @Override
    public Party getOwner() {
        return owner;
    }

    /**
     * Returns the parent location types (location types of which this location type is a sub-division).
     * A location type can have more than one possible parent. For example the parent of a location type named
     * <cite>“street”</cite> could be <cite>“locality”</cite>, <cite>“town”</cite> or <cite>“administrative area”</cite>.
     *
     * @return parent location types, or an empty collection if none.
     */
    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")         // Because unmodifiable
    public Collection<LocationType> getParents() {
        return parents;
    }

    /**
     * Returns the child location types (location types which sub-divides this location type).
     *
     * @return child location types, or an empty collection if none.
     */
    @Override
    @SuppressWarnings("ReturnOfCollectionOrArrayField")         // Because unmodifiable
    public Collection<LocationType> getChildren() {
        return children;
    }
}
