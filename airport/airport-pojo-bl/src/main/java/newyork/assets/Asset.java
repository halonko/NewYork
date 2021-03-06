package newyork.assets;

import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.expr;
import static ua.com.fielden.platform.entity.query.fluent.EntityQueryUtils.select;

import newyork.tablescodes.assets.AssetClass;
import newyork.tablescodes.assets.AssetOwnership;
import newyork.tablescodes.assets.AssetType;
import newyork.tablescodes.assets.AssetTypeOwnership;
import ua.com.fielden.platform.entity.ActivatableAbstractEntity;
import ua.com.fielden.platform.entity.DynamicEntityKey;
import ua.com.fielden.platform.entity.query.model.EntityResultQueryModel;
import ua.com.fielden.platform.entity.query.model.ExpressionModel;

import ua.com.fielden.platform.entity.annotation.CompanionObject;
import ua.com.fielden.platform.entity.annotation.CompositeKeyMember;
import ua.com.fielden.platform.entity.annotation.DescRequired;
import ua.com.fielden.platform.entity.annotation.DescTitle;
import ua.com.fielden.platform.entity.annotation.DisplayDescription;
import ua.com.fielden.platform.entity.annotation.IsProperty;
import ua.com.fielden.platform.entity.annotation.KeyTitle;
import ua.com.fielden.platform.entity.annotation.KeyType;
import ua.com.fielden.platform.entity.annotation.Calculated;
import ua.com.fielden.platform.entity.annotation.MapEntityTo;
import ua.com.fielden.platform.entity.annotation.MapTo;
import ua.com.fielden.platform.entity.annotation.Observable;
import ua.com.fielden.platform.entity.annotation.Readonly;
import ua.com.fielden.platform.entity.annotation.Required;
import ua.com.fielden.platform.entity.annotation.Title;
import ua.com.fielden.platform.entity.annotation.titles.PathTitle;
import ua.com.fielden.platform.entity.annotation.titles.Subtitles;
import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.utils.Pair;

/**
 * Master entity object.
 *
 * @author NewYork
 *
 */

@KeyType(DynamicEntityKey.class)
@KeyTitle("Asset Number")
@CompanionObject(IAsset.class)
@MapEntityTo
@DescTitle("Description")
@DisplayDescription
@DescRequired
public class Asset extends ActivatableAbstractEntity<DynamicEntityKey> {

    private static final Pair<String, String> entityTitleAndDesc = TitlesDescsGetter.getEntityTitleAndDesc(Asset.class);
    public static final String ENTITY_TITLE = entityTitleAndDesc.getKey();
    public static final String ENTITY_DESC = entityTitleAndDesc.getValue();

    @IsProperty
    @MapTo
    @Title(value = "Number", desc = "A unique asset number, auto-generated")
    @CompositeKeyMember(1)
    @Readonly
    private String number;
    
    @IsProperty
    @Title(value = "Fin Det", desc = "Financial details for this asset")
    private AssetFinDet finDet;

    @IsProperty
    @Readonly
    @Calculated
    @Title(value = "Current Ownership", desc = "Desc")
    @Subtitles({@PathTitle(path ="role", title="Ownership Role"),
                @PathTitle(path ="bu", title="Ownership Business Unit"),
                @PathTitle(path ="org", title="Ownership Organisation"),
                @PathTitle(path ="startDate", title="Ownership Start Date")})
    private AssetOwnership currOwnership;
    private static final EntityResultQueryModel<AssetOwnership> subQuery = select(AssetOwnership.class).where()
            .prop("asset").eq().extProp("asset").and()
            .prop("startDate").le().now().and()
            .prop("startDate").gt().extProp("startDate").model();


    protected static final ExpressionModel currOwnership_ = expr().model(select(AssetOwnership.class).where().prop("asset").eq().extProp("id").and().prop("startDate").le().now().and().notExists(subQuery).model()).model();

    @Observable
    protected Asset setCurrOwnership(final AssetOwnership name) {
        this.currOwnership = name;
        return this;
    }

    public AssetOwnership getCurrOwnership() {
        return currOwnership;
    }
    
    @IsProperty
    @MapTo
    @Title(value = "Type", desc = "A type of asset")
    @Required
    @Subtitles({@PathTitle(path ="currOwnership.role", title="Type Ownership Role"),
                @PathTitle(path ="currOwnership.bu", title="Type Ownership Business Unit"),
                @PathTitle(path ="currOwnership.org", title="Type Ownership Organisation"),
                @PathTitle(path ="currOwnership.startDate", title="Type Ownership Start Date")})
    private AssetType assetType;
    
    @Observable
    protected Asset setFinDet(final AssetFinDet finDet) {
        this.finDet = finDet;
        return this;
    }

    public AssetFinDet getFinDet() {
        return finDet;
    }

    @Observable
    public Asset setNumber(final String number) {
        this.number = number;
        return this;
    }

    public String getNumber() {
        return number;
    }
    
    @Override
    @Observable
    public Asset setDesc(final String desc) {
        super.setDesc(desc);
        return this;
    } 
    
    @Observable
    public Asset setAssetType(final AssetType assetType) {
        this.assetType = assetType;
        return this;
    }
    
    public AssetType getAssetType() {
        return assetType;
    }
    
    @Override
    @Observable
    public Asset setActive(boolean active) {
        super.setActive(active);
        return this;
    }
    


}
