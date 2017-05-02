package org.gluu.oxtrust.action;

import org.gluu.oxtrust.ldap.service.SectorIdentifierService;
import org.gluu.oxtrust.model.OxAuthSectorIdentifier;
import org.gluu.oxtrust.util.OxTrustConstants;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.seam.annotations.Logger;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.international.StatusMessages;
import org.slf4j.Logger;
import org.xdi.util.Util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Action class for search sector identifiers
 *
 * @author Javier Rojas Blum
 * @version January 15, 2016
 */
@Named("searchSectorIdentifierAction")
@ConversationScoped
//TODO CDI @Restrict("#{identity.loggedIn}")
public class SearchSectorIdentifierAction implements Serializable {

    private static final long serialVersionUID = -5270460481895022455L;

    @Inject
    private Logger log;

    @Inject
    StatusMessages statusMessages;

    @NotNull
    @Size(min = 0, max = 30, message = "Length of search string should be between 0 and 30")
    private String searchPattern;

    private String oldSearchPattern;

    private List<OxAuthSectorIdentifier> sectorIdentifierList;

    @Inject
    private SectorIdentifierService sectorIdentifierService;

    //TODO CDI @Restrict("#{s:hasPermission('sectorIdentifier', 'access')}")
    public String start() {
        return search();
    }

    //TODO CDI @Restrict("#{s:hasPermission('sectorIdentifier', 'access')}")
    public String search() {
        if (Util.equals(this.oldSearchPattern, this.searchPattern)) {
            return OxTrustConstants.RESULT_SUCCESS;
        }

        try {
            this.sectorIdentifierList = sectorIdentifierService.searchSectorIdentifiers(this.searchPattern, OxTrustConstants.searchSectorIdentifierSizeLimit);
            log.debug("Found \"" + this.sectorIdentifierList.size() + "\" sector identifiers.");
            this.oldSearchPattern = this.searchPattern;
        } catch (Exception ex) {
            log.error("Failed to find sector identifiers", ex);
            return OxTrustConstants.RESULT_FAILURE;
        }

        return OxTrustConstants.RESULT_SUCCESS;
    }

    public String getSearchPattern() {
        return searchPattern;
    }

    public void setSearchPattern(String searchPattern) {
        this.searchPattern = searchPattern;
    }

    public List<OxAuthSectorIdentifier> getSectorIdentifierList() {
        return sectorIdentifierList;
    }
}
