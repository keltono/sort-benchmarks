# Releasing New Versions

These notes are for the project maintainers to help with releasing artifacts to GitHub packages.

## One-time setup

1. Edit your `~/.m2/settings.xml` file to have a server element corresponding to GitHub:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>$username</username>
      <password>$password</password>
    </server>
  </servers>
</settings>
```

Replace `$username` with your GitHub username and `$password` with a token created that has `write:packages` permission. 
You can create this token on the GitHub website via `Settings` > `Developer Settings` > `Personal Access Tokens`.

2. Configure PGP keys locally using GnuPG (aka GPG) so that artifacts can be signed.

## Every Release

1. Make sure GPG can use TTY to get password. In bash:
```bash
export GPG_TTY=$(tty)
```

2. Prepare release:
```
mvn release:prepare -DpushChanges=false -DignoreSnapshots=true
```

The `-DpushChanges=false` avoids pushing the new release commits to GitHub, in case there's some problem that requires a rollback.
The `-DignoreSnapshots=true` allows `sort-benchmarks` to depend on a SNAPSHOT version of JQF/Mu2 for the latest API. 
Since these are not actually being bundled in the JAR, it should be fine as long as the latest version of Mu2/JQF is being used with
this release of `sort-benchmarks`.

The `-DpushChanges=false` avoids pushing the new release commits to GitHub, in case there's some problem that requires a rollback.

3. Check if everything went okay, then deploy:
```
mvn release:perform  -DlocalCheckout
```

The `-DlocalCheckout` is needed if changes were not pushed above. This tells Maven to use the new release from the local repo instead of pulling from GitHub.


4. If deploy was successful, you should see a new release under "Packages" in the GitHub repository. 
If so, you are good to push the release commit and the new SNAPSHOT commit to GitHub:

```
mvn push
```

If you encounter errors anywhere in the process, DO NOT PUSH the SNAPSHOT with the new version number. 
You can revert to pre-release by doing history rewriting and troublehsoot the issue before attempting to release again.
