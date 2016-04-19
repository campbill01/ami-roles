// file: jenkins_dsl/ami.groovy
//
// Amazon Linux AMI ID  http://aws.amazon.com/amazon-linux-ami/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
// Ubuntu 15.10 wily - https://cloud-images.ubuntu.com/locator/ec2/
// HVM (SSD) EBS-Backed 64-bit --  Region: US East N. Virginia
def ubuntu_ami = "ami-fce3c696"
def amazon_ami = "ami-60b6c60a"
def git_repo = "https://github.com/campbill01/ami-roles.git"

def amis = [  
              "ami-nexus":
                [
                  "name":"nexus",
                  "ami_profile":"nexus",
                  "branch" : "multi-os"
                ],
              "ami-tomcat7":
                [ 
                  "name":"tomcat7",
                  "ami_profile":"tomcat7",
                  "branch" : "multi-os"
                ],
              "ami-tomcat8":
                [ 
                  "name":"tomcat8",
                  "ami_profile":"tomcat8",
                  "branch" : "multi-os"
                ],
              "ami-jetty8":
                [ 
                  "name":"jetty8",
                  "ami_profile":"jetty8",
                  "branch" : "multi-os"
                ],
              "ami-base":
                [ 
                  "name":"base",
                  "ami_profile":"base",
                  "branch" : "multi-os"
                ],
              "ami-karyon":
                [ 
                  "name":"karyon",
                  "ami_profile":"karyon",
                  "branch" : "multi-os"
                ],
              "ami-haproxy":
                [
                  "name":"haproxy",
                  "ami_profile":"haproxy",
                  "branch" : "multi-os"
                ],
              "ami-jenkins":
                [
                  "name":"jenkins",
                  "ami_profile":"jenkins",
                  "branch" : "multi-os"
                ],
              "ami-jenkins2.0":
                [
                  "name":"jenkins2.0",
                  "ami_profile":"jenkins2.0",
                  "branch" : "multi-os"
                ],
              "ami-mongodb":
                [
                  "name":"mongodb",
                  "ami_profile":"mongodb",
                  "branch" : "multi-os"
                ],
              "ami-oraclejdk8":
                [
                  "name":"oraclejdk8",
                  "ami_profile":"oraclejdk8",
                  "branch" : "multi-os"
                ],
              "ami-jenkins-user":
                [
                  "name":"jenkins-user",
                  "ami_profile":"jenkins-user",
                  "branch" : "multi-os"
                ],
              "ami-nginx":
                [
                  "name":"nginx",
                  "ami_profile":"nginx",
                  "branch" : "multi-os"
                ],
              "ami-packer":
                [
                  "name":"packer",
                  "ami_profile":"packer",
                  "branch" : "multi-os"
                ]
            ]


amis.values().each { ami ->
def jobname = "ami-" + ami.name
  
  freeStyleJob(jobname) {

    steps {
      shell('$WORKSPACE/bin/provision_base_ami')
    }
      
    scm {
      git {
        remote {
          url(git_repo)
          branch(ami.branch)
          credentials("GitHub")
        }
      }
    }

    wrappers {
      preBuildCleanup()
    }

    publishers {
      archiveArtifacts { 
        pattern('AMI-$AMI_PROFILE')
      }
    }
    parameters {
      stringParam("UBUNTUAMI", ubuntu_ami, "Ubuntu amiid to be used")
      stringParam("AMAZONAMI", amazon_ami, "Amazon amiid to be used")
      stringParam("AMI_PROFILE", ami.ami_profile, "Profile to use")
    }
    
    parameters {
      choiceParam("OS", ['Both', 'Ubuntu','Amazon'], 'OS - Choices are Ubuntu or Amazon or build one of each')
    }
    
  }
}


