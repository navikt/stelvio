package no.nav.maven.plugin.batchclientplugin;


public class ArtifactItem
{

    public ArtifactItem()
    {
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    private String groupId;
    private String artifactId;

}
